'use strict';

const BOOKING_KIND_IDS = ['service', 'master', 'date'];
let user;
let sectionKind;

async function initPage() {
    showBookingBy('info');
    const response = await fetch('../index');
    if (response.ok) {
        try {
            const json = await response.json();
            user = json.user;
        } catch (e) {
            user.id = 0;
            console.error("Server not response: " + e.message);
        }
    } else {
        user.id = 0;
        console.error("HTTP error: " + response.status);
    }
    initUserButtons();
}
function initUserButtons() {
    let isShowForGuest = isGuest();
    let guestBtns = document.getElementsByClassName("guestBtn");
    let userBtns = document.getElementsByClassName("userBtn");
    for(let btn of guestBtns){
        btn.style.display = isShowForGuest ? 'block' : "none";
    }
    for(let btn of userBtns){
        btn.style.display = isShowForGuest ? 'none' : "block";
    }
}
function isGuest() {
    return user.id <= 0 || user.name == ""
        || user.name == null || user.status == "Guest"
}
function showBookingBy(bookingKind) {
    for (let sec of BOOKING_KIND_IDS) {
        document.getElementById(`${sec}`).style.display = 'none';
    }
    if(bookingKind != null || bookingKind != ""){
        document.getElementById(`${bookingKind}`).style.display = 'block';
        sectionKind = bookingKind;
        initBooking(bookingKind, 0);
    }
}

async function initBooking(bookingKind, choice) {
    const response = await fetch(`../booking?kind=${bookingKind}&choice=${choice}`);
    if(response.ok){
        try{
            const json = await response.json();
            console.log(json);
            switch (bookingKind) {
                case 'service': {
                    printServices(`${sectionKind}-category`, `${sectionKind}-service`, json);
                    break;
                }
                case 'master': {
                    printMasters(`${sectionKind}-master`, json);
                    break;
                }
                case 'date':{
                    printDates(`${sectionKind}-date`, `${sectionKind}-time`, json)
                    break;
                }
            }
        } catch (e) {
            console.error("Server not response: " + e.message);
        }
    } else {
        console.error("HTTP error: " + response.status);
    }
}

function onClickChoose(idName) {
    document.getElementById(idName).style.display = 'block';
}
function getElement(id, value, onclick, display) {
    const tag = document.createElement('h3');
    tag.setAttribute('id', id );
    tag.innerHTML = value;
    tag.setAttribute('onclick', onclick);
    tag.style.display = display;
    const li = document.createElement('li');
    li.appendChild(tag);
    return li;
}

function printServices(parDivId, childDivId, map) {
    const catDiv = document.getElementById(parDivId);
    catDiv.innerHTML = "";
    const serDiv = document.getElementById(childDivId);
    serDiv.innerHTML = "";
    const catUl = document.createElement('ul');
    for(let id =0; id < map.services.length; id++){
        let cat = map.services[id];
        catUl.appendChild(getElement(id, cat.category, `onClickChoose(\'${cat.category}\')`, 'block'));
        const servUl = document.createElement('ul');
        servUl.setAttribute('id', cat.category);
        servUl.style.display = 'none';
        for(let serv of cat.services){
            if(serv != null){
                servUl.appendChild(getElement(serv.id, serv.name, `initBooking(\'master\',\'${serv.id}\')`, 'block'));
            }
        }
        serDiv.appendChild(servUl);
    }
    catDiv.appendChild(catUl);
}
function printMasters(masterDivId, map) {
    const mastDiv = document.getElementById(masterDivId);
    mastDiv.innerHTML = '';
    const ul = document.createElement('ul');
    console.log(map);
    for(let id=0; id<map.masters.length; id++){
        let master = map.masters[id];
        ul.appendChild(getElement(master.id, master.name, `initBooking(\'date\',\'${master.id}\')`, 'block'));
    }
    mastDiv.appendChild(ul);
}
function printDates(parDivId, childDivId, map) {
    const catDiv = document.getElementById(parDivId);
    catDiv.innerHTML = "";
    const serDiv = document.getElementById(childDivId);
    serDiv.innerHTML = "";
    const catUl = document.createElement('ul');
    for(let id =0; id < map.dates.length; id++){
        let cat = map.dates[id];
        catUl.appendChild(getElement(id, cat.date, `onClickChoose(\'${cat.date}\')`, 'block'));
        const servUl = document.createElement('ul');
        servUl.setAttribute('id', cat.date);
        servUl.style.display = 'none';
        for(let serv of cat.times){
            if(serv != null){
                servUl.appendChild(getElement(serv.id, serv.time, `initBooking(\'master\',\'${serv.id}\')`, 'block'));
            }
        }
        serDiv.appendChild(servUl);
    }
    catDiv.appendChild(catUl);
}



