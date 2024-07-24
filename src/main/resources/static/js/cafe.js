
document.addEventListener('DOMContentLoaded', function() {

    const menuContainer = document.getElementById('menu-container');
    const inputs = document.querySelectorAll('input');

    inputs.forEach(input => {
        input.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                e.preventDefault();
            }
        });
    });

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
    document.getElementById('menu-container').appendChild(newSection);
}



function getCurrentMenuCount() {
    const rows = document.querySelectorAll('#menu-container .row.mb-3');
    console.log(rows);
    if(rows.length === 0) return 0;
    const lastRow = rows[rows.length - 1];
    console.log(lastRow);
    const inputElement = lastRow.querySelector('input[type="file"]');

    if (inputElement) {
        const id = inputElement.id;
        const match = id.match(/menuImageFile(\d+)/);
        if (match) return parseInt(match[1], 10);
    }
}


function deleteMenu(menuId, idx) {
    const buttonElement = document.querySelector(`#deleteMenuButton${idx}`);
    if (!confirm("정말 삭제하시겠습니까?")) {
        return;
    }

    if(menuId !== null) {
        let xhr = new XMLHttpRequest();
        xhr.open('DELETE', '/admin/menu/' + menuId, true);
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

                startHourInput.value = startHour;
                startMinuteInput.value = startMinute;
                endHourInput.value = endHour;
                endMinuteInput.value = endMinute;
            }
        }
    });
}

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
        console.log(startHours[i]);
        const hourObject = {
            day: days[i],
            startHour: startHours[i] || 0,
            startMinute: startMinutes[i] || 0,
            endHour: endHours[i] || 0,
            endMinute: endMinutes[i] || 0
        }

        hours.push(hourObject)
    }

    return hours;
}