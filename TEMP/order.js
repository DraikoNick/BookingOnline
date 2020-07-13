'use strict';

const SERVICE_LINE = {
    title : 'По Услуге',
    line : ['service-service', 'service-date','service-time', 'service-master'],
    names : ['УСЛУГА', 'ДАТА', 'ВРЕМЯ', 'МАСТЕР']
}
const MASTER_LINE = {
    title : 'По Мастеру',
    line : ['master-master', 'master-service', 'master-date', 'master-time'],
    names : ['МАСТЕР', 'УСЛУГА', 'ДАТА', 'ВРЕМЯ']
}
const DATE_LINE = {
    title : 'По Дате и Времени',
    line : ['date-date', 'date-time', 'date-service', 'date-master'],
    names : ['ДАТА', 'ВРЕМЯ', 'УСЛУГА', 'МАСТЕР']
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

