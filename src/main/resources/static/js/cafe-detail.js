

function showImage(button, prefix) {
    let index = button.getAttribute('data-index');
    let elementId = prefix + index;
    let element = document.getElementById(elementId);

    return element;
}

document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("cafe-form").addEventListener("submit", async function(event) {
        event.preventDefault();

        const cafeId = document.getElementById('cafeId').value;
        const name = document.getElementById('cafeName').value;
        const address = document.getElementById('cafeAddress').value;
        const phoneNumber = document.getElementById('cafePhoneNumber').value;

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
                resolve(null);
            }
        });

        const mainImageBase64 = await processMainImage;

        const menus = await processMenus();
        if (menus === null) return;

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

        showLoading();

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

            fetch(`/admin/cafe/${cafeId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(data)
            })
                .then(response => response.json())
                .then(data => {
                    hideLoading();
                    if(data.redirectUrl) {
                        window.location.href = data.redirectUrl;
                    }
                })
                .catch(error => {
                    hideLoading();
                    console.error('Error:', error);
                });
        });
    });
});



