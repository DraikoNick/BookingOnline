'use strict';

let user;
let serviceId, masterId, dateId, timeId;

async function getJson(url){
    const response = await fetch(url);
    if(response.ok){
        try{
            const json = await response.json();
            return json;
        }catch (e) {
            return null;
            console.error("Server not response: " + e.message);
        }
    } else {
        return null;
        console.error("HTTP error: " + response.status);
    }
}
async function initPage() {
    const json = await getJson('../index');
    if(json != null){
        user = json.user;
    }else{
        user.id = 0;
    }
    let isShowForGuest = user.id <= 0 || user.name == "" || user.name == null || user.status == "Guest";
    let guestBtns = document.getElementsByClassName("guestBtn");
    let userBtns = document.getElementsByClassName("userBtn");
    for(let btn of guestBtns){
        btn.style.display = isShowForGuest ? 'block' : "none";
    }
    for(let btn of userBtns){
        btn.style.display = isShowForGuest ? 'none' : "block";
    }
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
function getCleanSelector(){
    const selector = document.getElementById('selector');
    selector.innerHTML = '';
    return selector;
}
function onClickChoose(kind, choose, saveTo){
    switch (saveTo) {
        case 'service':{
            serviceId = choose;
        }
        case 'master':{
            masterId = choose;
        }
        case 'date':{
            dateId = choose;
        }
        case 'time':{
            timeId = choose;
        }
    }
    initSelector(kind, choose, 'master');
}

async function initSelector(selectorType, choice, nextSelector){
    const json = await getJson(`../booking?kind=${selectorType}&choice=${choice}`);
    const bookBy = document.getElementById('bookBy');
    switch (selectorType) {
        case 'service': {
            printServices(json, nextSelector);
            bookBy.innerHTML = 'По услуге';
            break;
        }
        case 'master': {
            printMasters(json, nextSelector);
            bookBy.innerHTML = 'По Мастеру';
            break;
        }
        case 'date':{
            printDate(json,nextSelector);
            bookBy.innerHTML = 'По Времени';
            break;
        }
    }
}

function printServices(json, nextSelector) {
    const selector = getCleanSelector();
    let map = json.services;
    const ul = document.createElement('ul');
    for(let category of map){
        for(let service of category.services){
            ul.appendChild(getElement(service.id, service.name, `onClickChoose(\'${nextSelector}\', ${service.id})`, 'block'));
        }
    }
    selector.appendChild(ul);
}
function printMasters(json, nextSelector) {
    const selector = getCleanSelector();
    let map = json.masters;
    const ul = document.createElement('ul');
    for(let master of map){
        ul.appendChild(getElement(master.id, master.name, `onClickChoose(\'${nextSelector}\', ${master.id})`, 'block'));
    }
    selector.appendChild(ul);
}
function printDate(json, nextSelector) {
    const selector = getCleanSelector();
    let map = json.dates;
    const ul = document.createElement('ul');
    for(let date of map){
        for(let time of date.times){
            ul.appendChild(getElement(time.id, time.time, `onClickChoose(\'${nextSelector}\', ${time.id})`, 'block'));
        }
    }
    selector.appendChild(ul);
}



