'use strict';

let user;

async function getJson(url){
    const response = await fetch(uri);
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
async function getListOf(bookingKind, choice) {
    const json = await getJson(`../booking?kind=${bookingKind}&choice=${choice}`);
    let list;
    switch (bookingKind) {
        case 'service': {
            list = json.services;
            break;
        }
        case 'master': {
            list = json.masters;
            break;
        }
        case 'date':{
            list = json.dates;
            break;
        }
    }
    return list;
}

function getSectionHead(sectionName) {
    const table = document.getElementById('book-table');
    table.innerHTML = '';
    const printTable = function(sectionName, parentElement){
        let names;
        let title;
        let ids;
        switch (sectionName) {
            case 'service':{
                names = ['УСЛУГА', 'ДАТА', 'ВРЕМЯ', 'МАСТЕР'];
                title = 'По Услуге';
                ids = ['service-service', 'service-date','service-time', 'service-master'];
                break;
            }
            case 'master':{
                names = ['МАСТЕР', 'УСЛУГА', 'ДАТА', 'ВРЕМЯ'];
                title = 'По Мастеру';
                ids = ['master-master', 'master-service', 'master-date', 'master-time'];
                break;
            }
            case 'date':{
                names = ['ДАТА', 'ВРЕМЯ', 'УСЛУГА', 'МАСТЕР'];
                title = 'По Дате и Времени';
                ids = ['date-date', 'date-time', 'date-service', 'date-master'];
                break;
            }
        }
        const tElem = document.createElement('h3');
        tElem.innerHTML = title;
        parentElement.appendChild(tElem);
        const aside = document.createElement('aside');
        for(let name of names){
            const div = document.createElement('div');
            div.setAttribute('class', 'unit-1of4');
            div.innerHTML = name;
            aside.appendChild(div);
        }
        parentElement.appendChild(aside);
        const aside2 = document.createElement('aside');
        for(let id of ids){
            const div = document.createElement('div');
            div.setAttribute('class', 'unit-1of4');
            div.setAttribute('id', id);
            aside2.appendChild(div);
        }
        parentElement.appendChild(aside2);
    }
    printTable(sectionName, table);
}

function printList(list, listType, nextSection) {
    let toSection = '';
    let toSectionCh = '';
    let toSectionCh2 = '';
    switch (listType) {
        case 'service':{
            toSection = 'category';
            toSectionCh = 'service';
            toSectionCh2 = 'services';
            break;
        }
        case 'master':{
            toSection = 'master';
            toSectionCh2 = 'masters';
            break;
        }
        case 'date':{
            toSection = 'date';
            toSectionCh = 'time';
            toSectionCh2 = 'times';
            break;
        }
    }
    const getElement = function (id, value, onclick, display) {
        const tag = document.createElement('h3');
        tag.setAttribute('id', id );
        tag.innerHTML = value;
        tag.setAttribute('onclick', onclick);
        tag.style.display = display;
        const li = document.createElement('li');
        li.appendChild(tag);
        return li;
    }
    const printDoubleList = function () {
        const parent = document.getElementById(toSection);
        const child = document.getElementById(toSectionCh);
        const ul = document.createElement('ul');
        for(let i=0; i < list.length; i++){
            let element = list[i][`${toSection}`]
            ul.appendChild(getElement(i, element, `onClickOpen(\'${element}\')`, 'block'));
            const secUl = document.createElement('ul');
            secUl.setAttribute('id', element);
            secUl.style.display = 'none';
            let secList = list[i][`${toSectionCh2}`];
            for(let e of secList){
                secUl.appendChild(getElement(e.id, e.name, `getNext(${nextSection}, ${e.id})`));
            }
            child.appendChild(secUl);
        }
        parent.appendChild(child);
    }
    const printOneList = function () {
        const parent = document.getElementById(toSection);
        const ul = document.createElement('ul');
        for(let id=0; id<list.length; id++){
            let elem = list[id][toSection];
            ul.appendChild(getElement(elem.id, elem.name, `getNext(${nextSection}, ${elem.id})`));
        }
        parent.appendChild(ul);
    }

}