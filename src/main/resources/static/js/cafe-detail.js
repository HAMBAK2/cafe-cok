
function disableButtons(index, prefixView, prefixDelete) {
    document.getElementById(prefixView + index).disabled = true;
    document.getElementById(prefixDelete + index).disabled = true;
}

function showImage(button, prefix) {
    let index = button.getAttribute('data-index');
    console.log(index);
    let elementId = prefix + index;
    let element = document.getElementById(elementId);

    return element;
}

function getCurrentImageCount() {
    return document.querySelectorAll('#image-container .row.mb-3').length;
}

function getCurrentMenuCount() {
    const rows = document.querySelectorAll('#menu-container .row.mb-3');
    console.log(rows);
    const lastRow = rows[rows.length - 1];
    console.log(lastRow);
    const inputElement = lastRow.querySelector('input[type="file"]');

    if (inputElement) {
        const id = inputElement.id;
        const match = id.match(/menuImageFile(\d+)/);
        if (match) return parseInt(match[1], 10);
    }
}

function addMenuList() {
    let menuCount = getCurrentMenuCount() + 1;
    const newSection = document.createElement('div');
    newSection.className = 'row mb-3';
    newSection.innerHTML = `
                <div class="col-md-3">
                    <input type="file" class="form-control" id="menuImageFile${menuCount}">
                </div>
                <div class="col-md-2">
                    <input type="text" class="form-control" id="menuName${menuCount}" placeholder="메뉴명">
                </div>
                <div class="col-md-1">
                    <input type="text" class="form-control" id="menuPrice${menuCount}" placeholder="가격">
                </div> 
                <div class="col-md-4 btn-container"> 
                    <button type="button" class="btn btn-danger" id="deleteMenuButton${menuCount}" data-idx="${menuCount}">추가 메뉴 삭제</button> 
                </div>
            `;
    console.log(newSection);
    document.getElementById('menu-container').appendChild(newSection);
}

document.addEventListener('DOMContentLoaded', function() {
    const menuContainer = document.getElementById('menu-container');
    if (menuContainer) {
        menuContainer.addEventListener('click', function(event) {
            if (event.target && event.target.classList.contains('btn-danger')) {
                const idx = event.target.getAttribute('data-idx');
                const menuId = event.target.getAttribute('data-menu-id');

                if(menuId !== null) deleteMenu(menuId, idx);
                else deleteMenu(null, idx);
            }
        });
    } else {
        console.error('menu-container 요소를 찾을 수 없습니다.');
    }
});


function deleteMenu(menuId, idx) {
    const buttonElement = document.querySelector(`#deleteMenuButton${idx}`);
    console.log("진입");
    console.log(menuId + "abc");
    console.log("abc");
    if (!confirm("정말 삭제하시겠습니까?")) {
        return;
    }

    if(menuId !== null) {
        let xhr = new XMLHttpRequest();
        xhr.open('DELETE', '/admin/menu/delete/' + menuId, true);
        xhr.setRequestHeader('Content-Type', 'application/json');

        xhr.onreadystatechange = function () {
            if(xhr.readyState === 4) {
                if(xhr.status === 200) {
                    alert(xhr.responseText);
                } else {
                    alert('메뉴 삭제 중 오류가 발생했습니다.')
                }
            }
        };

        xhr.send();
    }

    const rowDiv = buttonElement.closest('.row.mb-3');
    rowDiv.remove();
}


function selectApply() {
    const checkboxes = document.querySelectorAll('.form-check-input');
    const selectedDays = [];

    checkboxes.forEach(checkbox => {
        if (checkbox.checked) {
            selectedDays.push(checkbox.value);
        }
    });

    const startHour = document.querySelector('input[placeholder="시작 시간"]').value;
    const startMinute = document.querySelector('input[placeholder="시작 분"]').value;
    const endHour = document.querySelector('input[placeholder="종료 시간"]').value;
    const endMinute = document.querySelector('input[placeholder="종료 분"]').value;

    const spans = document.querySelectorAll('.day-span');

    spans.forEach((span, index) => {

        if(selectedDays.includes(span.textContent)) {
            const parentDiv = span.closest('.row.input-group');

            if (parentDiv) {
                // 해당 부모 div 내의 start-hour 및 start-minute input 요소 선택
                const startHourInput = parentDiv.querySelector(`#start-hour`);
                const startMinuteInput = parentDiv.querySelector(`#start-minute`);
                const endHourInput = parentDiv.querySelector(`#end-hour`);
                const endMinuteInput = parentDiv.querySelector(`#end-minute`);
                console.log(startHourInput);
                console.log(startMinuteInput);

                startHourInput.value = startHour;
                startMinuteInput.value = startMinute;
                endHourInput.value = endHour;
                endMinuteInput.value = endMinute;
            }
        }
    });
}

document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("cafe-form").addEventListener("submit", async function(event) {
        event.preventDefault(); // 폼 제출을 막음

        const cafeId = document.getElementById('cafeId').value;
        const name = document.getElementById('cafeName').value;
        const address = document.getElementById('cafeAddress').value;
        const phoneNumber = document.getElementById('cafePhoneNumber').value;

        // 메인 이미지 파일 처리
        const mainImageFile = document.getElementById('cafeMainImageFile').files[0];
        const mainImageId = document.getElementById('cafeMainImageFile').getAttribute('data-image-id');
        const reader = new FileReader();

        const processMainImage = new Promise((resolve) => {
            if (mainImageFile) {
                reader.onloadend = function() {
                    const mainImageBase64 = reader.result.split(',')[1];
                    resolve(mainImageBase64);
                };
                reader.readAsDataURL(mainImageFile);
            } else {
                resolve(null); // mainImageFile이 없으면 null을 반환
            }
        });

        const mainImageBase64 = await processMainImage;

        const menus = await processMenus();
        if (menus === null) return; // 메뉴 처리 중 문제가 발생한 경우 종료

        const otherImageElements = document.querySelectorAll('input[type="file"][id^="cafeOtherImageFile"]');
        const otherImagesBase64Promises = Array.from(otherImageElements).map((input) => {
            const imageId = input.getAttribute('data-image-id');
            if (input.files[0]) {
                return new Promise((resolve) => {
                    const reader = new FileReader();
                    reader.onloadend = function() {
                        const base64Image = reader.result.split(',')[1];
                        resolve({id: imageId, imageBase64: base64Image});
                    };
                    reader.readAsDataURL(input.files[0]);
                });
            } else {
                return Promise.resolve(null);
            }
        });

        Promise.all(otherImagesBase64Promises).then((otherImagesBase64) => {
            const data = {
                cafeId,
                name,
                address,
                phoneNumber,
                image: { id: mainImageId, imageBase64:  mainImageBase64},
                otherImages: otherImagesBase64.filter(img => img !== null),
                menus: menus,
                hours: generateHours()
            };

            fetch(`/admin/cafes/${cafeId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(data)
            })
                .then(response => response.json())
                .then(data => {
                    if(data.redirectUrl) {
                        window.location.href = data.redirectUrl;
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        });
    });
});

async function processMenus() {
    const menuContainers = document.querySelectorAll('#menu-container > div');
    const menus = [];
    for (const container of menuContainers) {
        let id = container.querySelector('[id^="menuId"]');
        let name = container.querySelector('[id^="menuName"]').value;
        let price = container.querySelector('[id^="menuPrice"]').value;
        let imageFile = container.querySelector('[id^="menuImageFile"]').files[0];

        if (id !== null) id = id.value;
        if (!name) {
            alert("메뉴명은 필수입니다.");
            return null;
        }
        if(!price) {
            alert("가격은 필수입니다.");
            return null;
        }

        let imageBase64 = null;
        if (imageFile) {
            imageBase64 = await new Promise((resolve, reject) => {
                const reader = new FileReader();
                reader.onloadend = function() {
                    resolve(reader.result.split(',')[1]);
                };
                reader.onerror = reject;
                reader.readAsDataURL(imageFile);
            });
        }

        menus.push({ id, name, price, image: imageBase64 });
    }
    return menus;
}

function generateHours() {

    const hourRows = document.getElementById('selected-time-group');
    const daySpanElements = document.querySelectorAll('.day-span');
    const startHourElements = document.querySelectorAll('.form-control.start-hour');
    const startMinuteElements = document.querySelectorAll('.form-control.start-minute');
    const endHourElements = document.querySelectorAll('.form-control.end-hour');
    const endMinuteElements = document.querySelectorAll('.form-control.end-minute');
    const days = Array.from(daySpanElements).map(element => element.textContent.trim());
    const startHours = Array.from(startHourElements).map(element => element.value.trim());
    const startMinutes = Array.from(startMinuteElements).map(element => element.value.trim());
    const endHours = Array.from(endHourElements).map(element => element.value.trim());
    const endMinutes = Array.from(endMinuteElements).map(element => element.value.trim());


    const hours = [];
    for(let i = 0; i < 7; i++) {
        const hourObject = {
            day: days[i],
            startHour: startHours[i],
            startMinute: startMinutes[i],
            endHour: endHours[i],
            endMinute: endMinutes[i]
        }

        hours.push(hourObject)
    }

    return hours;
}