document.addEventListener('DOMContentLoaded', function() {
    const inputField = document.getElementById('search-cafe-name');
    const resultsContainer = document.getElementById('searchResults');

    inputField.addEventListener('input', function() {
        const query = inputField.value.trim();
        if (query.length > 0) {
            fetchCafeResults(query);
        } else {
            resultsContainer.innerHTML = ''; // Clear results if query is empty
        }
    });

    function fetchCafeResults(query) {

        fetch('/admin/app-key')
            .then(response => response.text())  // text()로 변경하여 문자열로 응답을 받음
            .then(appKey => {
                const apiUrl = `https://dapi.kakao.com/v2/local/search/keyword.json?query=${encodeURIComponent(query)}`;

                fetch(apiUrl, {
                    headers: {
                        'Authorization': `KakaoAK ${appKey}` // 서버에서 받은 appKey 사용
                    }
                })
                    .then(response => response.json())
                    .then(data => {
                        displayResults(data.documents);
                    })
                    .catch(error => {
                        console.error('Error fetching data:', error);
                        resultsContainer.innerHTML = '<p>검색 결과를 불러오는 데 실패했습니다.</p>';
                    });
            })
            .catch(error => {
                console.error('Error fetching appKey:', error);
                resultsContainer.innerHTML = '<p>App Key를 가져오는 데 실패했습니다.</p>';
            });
    }

    function displayResults(results) {
        if (results.length > 0) {
            let html = '<ul class="list-group">';
            results.forEach(result => {
                html += `<li class="list-group-item" 
                    data-place-name = "${result.place_name}" data-address-name = "${result.address_name}"
                    data-phone = "${result.phone}" data-id = "${result.id}" data-y = "${result.y}" data-x = "${result.x}">
                        ${result.place_name} | ${result.address_name}
                    </li>`;
            });
            html += '</ul>';
            resultsContainer.innerHTML = html;

            const listItems = document.querySelectorAll('#searchResults .list-group-item');
            listItems.forEach(item => {
                item.addEventListener('click', function() {
                    const placeName = this.getAttribute('data-place-name');
                    const addressName = this.getAttribute('data-address-name');
                    const phone = this.getAttribute('data-phone');
                    const id = this.getAttribute('data-id');
                    const y = this.getAttribute('data-y');
                    const x = this.getAttribute('data-x');

                    checkCafeExists(id)
                        .then(exists => {
                            if (!exists) {
                                insertValue(placeName, addressName, phone, id, y, x);
                            } else {
                                alert("이미 등록된 카페입니다.")
                                return;
                            }
                        })
                        .catch(error => {
                            console.error('오류 발생:', error);
                        });

                    resultsContainer.innerHTML = '';
                });
            });
        } else {
            resultsContainer.innerHTML = '<p>검색 결과가 없습니다.</p>';
        }
    }

    function insertValue(placeName, addressName, phone, id, y, x) {

        document.getElementById('cafeName').value = placeName;
        document.getElementById('cafeAddress').value = addressName;
        document.getElementById('cafePhoneNumber').value = phone;
        document.getElementById('cafeLatitude').value = y;
        document.getElementById('cafeLongitude').value = x;
        document.getElementById('kakao-cafe-id').value = id;
    }

    function checkCafeExists(kakaoId) {
        return fetch(`/admin/cafe/exists/` + kakaoId)
            .then(response => {
                if (!response.ok) {
                    throw new Error('네트워크 응답이 올바르지 않습니다.');
                }
                return response.json();
            })
            .then(data => {
                return data; // data는 boolean 값입니다.
            })
            .catch(error => {
                console.error('카페 존재 여부 확인 중 오류 발생:', error);
                return false; // 오류 발생 시 false 반환
            });
    }
});

document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("cafe-register-form").addEventListener("submit", async function(event) {
        event.preventDefault(); // 폼 제출을 막음

        const name = document.getElementById('cafeName').value;
        const address = document.getElementById('cafeAddress').value;
        const phone = document.getElementById('cafePhoneNumber').value;
        const latitude = document.getElementById('cafeLatitude').value;
        const longitude = document.getElementById('cafeLongitude').value;
        const kakaoId = document.getElementById('kakao-cafe-id').value;
        const mainImageFile = document.getElementById('cafeMainImageFile').files[0];
        const reader = new FileReader();

        const processMainImage = new Promise((resolve) => {
            if (mainImageFile) {
                reader.onloadend = function() {
                    const mainImageBase64 = reader.result.split(',')[1];
                    resolve(mainImageBase64);
                };
                reader.readAsDataURL(mainImageFile);
            } else {
                alert("카페의 메인 이미지는 필수 요소입니다.");
                return;
            }
        });

        const menus = await processMenus();
        if (menus === null) return;
        const mainImage = await processMainImage;
        const otherImageElements = document.querySelectorAll('input[type="file"][id^="cafeOtherImageFile"]');
        const otherImagesBase64Promises = Array.from(otherImageElements).map((input) => {
            if (input.files[0]) {
                return new Promise((resolve) => {
                    const reader = new FileReader();
                    reader.onloadend = function() {
                        const base64Image = reader.result.split(',')[1];
                        resolve(base64Image);
                    };
                    reader.readAsDataURL(input.files[0]);
                });
            } else {
                return Promise.resolve(null);
            }
        });

        Promise.all(otherImagesBase64Promises).then((otherImagesBase64) => {
            const data = {
                name,
                address,
                phone,
                latitude,
                longitude,
                kakaoId,
                mainImage,
                otherImages: otherImagesBase64.filter(img => img !== null),
                menus: menus,
                hours: generateHours()
            };

            fetch(`/admin/cafe`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(data)
            })
                .then(response => response.json())
                .then(data => {
                    if (data.redirectUrl) {
                        window.location.href = data.redirectUrl;
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        });
    });
});

