'use strict';

let user;
let order = {
    serviceId : '',     serviceName : '',
    masterId : '',      masterName  : '',
    date : '',          dateName    : '',
    time : '',          timeName    : ''
}
async function initPage() {
    const register = document.getElementById('register');
    register.style.display = 'none';
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

async function initBooking(kind, choice) {
    const register = document.getElementById('register');
    register.style.display = 'none';
    document.getElementById('chooser').innerHTML = '';
    let url = `../booking?kind=${kind}&choice=${choice}`;
    const json = await getJson(url);
    let type = json.type;
    let list = json.list;
    switch (type) {
        case 'services':{}
        case 'services3':{}
        case 'masters2':{}
        case 'orders2':{}
        case 'orders3':{}
        case 'masters':{
            printList(list, type);
            break;
        }
        case 'orders':{}
        case 'services2':{}
        case 'masters3':{}
        case 'dateForService':{
            printDateList(list, type);
            break;
        }
    }
}

function printList(list, type) {
    const chooser = document.getElementById('chooser');
    for(let elem of list){
        let btn = document.createElement('a');
        btn.setAttribute('class', 'btn btn-default');
        btn.setAttribute('id', elem.id);
        btn.setAttribute('onclick', `doSelect('${type}', ${elem.id}, '${elem.name}')`);
        btn.innerHTML = elem.name;
        chooser.appendChild(btn);
    }
}
function printDateList(list,type) {
    const chooser = document.getElementById('chooser');
    for(let elem of list){
        let dateField = document.getElementById(elem.date);
        if(dateField == null){
            dateField = document.createElement('div');
            dateField.setAttribute('class', 'unit-3of4');
            dateField.setAttribute('id', elem.date);
            let lable = document.createElement('div');
            lable.setAttribute('class', 'unit-1of4');
            let h3 = document.createElement('h3');
            h3.innerHTML = elem.date;
            lable.appendChild(h3);
            let section = document.createElement('section');
            section.appendChild(lable);
            section.appendChild(dateField);
            chooser.appendChild(section);
            dateField = document.getElementById(elem.date);
        }
        let dateTimeField = `${elem.date}_${elem.time}_${order.serviceId}`;
        let timeBtn = document.getElementById(dateTimeField);
        if(timeBtn == null){
            let btn = document.createElement('a');
            btn.setAttribute('class', 'btn btn-default');
            btn.setAttribute('id', dateTimeField);
            btn.innerHTML = elem.time;
            btn.setAttribute('onclick', `doSelect('${type}', '${dateTimeField}', '${dateTimeField}')`);
            dateField.appendChild(btn);
        }
    }
}

function doSelect(type, select, value) {
    let nextType = '';
    switch (type) {
        case 'services':{
            order.serviceId = select;
            order.serviceName = value;
            nextType = 'services2';
            break;
        }
        case 'services2':{
            let arr = select.split("_");
            order.date = arr[0];
            order.time = arr[1];
            let arrNames = value.split("_");
            order.dateName = arrNames[0];
            order.timeName = arrNames[1];
            nextType = 'services3';
            break;
        }
        case 'services3':{
            order.masterId = select;
            order.masterName = value;
            nextType = 'commit';
            break;
        }
        case 'masters':{
            order.masterId = select;
            order.masterName = value;
            nextType = 'masters2';
            break;
        }
        case 'masters2':{
            order.serviceId = select;
            order.serviceName = value;
            nextType = 'masters3';
            select = order.masterId;
            break;
        }
        case 'masters3':{
            let arr = select.split("_");
            order.date = arr[0];
            order.time = arr[1];
            let arrNames = value.split("_");
            order.dateName = arrNames[0];
            order.timeName = arrNames[1];
            nextType = 'commit';
            break;
        }
        case 'orders':{
            let arr = select.split("_");
            order.date = arr[0];
            order.time = arr[1];
            let arrNames = value.split("_");
            order.dateName = arrNames[0];
            order.timeName = arrNames[1];
            nextType = 'orders2';
            break;
        }
        case 'orders2':{
            order.serviceId = select;
            order.serviceName = value;
            nextType = 'orders3';
            break;
        }
        case 'orders3':{
            order.masterId = select;
            order.masterName = value;
            nextType = 'commit';
            break;
        }
    }
    console.log(order);
    if(nextType != 'commit'){
        initBooking(nextType, select);
        return;
    }
    printCommit();
}

function printCommit() {
    const register = document.getElementById('register');
    register.style.display = 'block';
    document.getElementById('chooser').innerHTML = '';
    document.getElementById('commitService').innerHTML = `Услуга: ${order.serviceName}`;
    document.getElementById('commitDate').innerHTML = `Дата: ${order.dateName}`;
    document.getElementById('commitTime').innerHTML = `Время: ${order.timeName}`;
    document.getElementById('commitMaster').innerHTML = `Мастер: ${order.masterName}`;
    document.commitForm.masterId.value = order.masterId;
    document.commitForm.serviceId.value = order.serviceId;
    document.commitForm.date.value = order.date;
    document.commitForm.time.value = order.time;
    if(user.id != null && user.id != 0){
        document.getElementById('newUserForm').style.display = 'none';
    }
}

async function doCommit() {
    document.commitForm.setAttribute('action', '../booking?timezone='+new Date().getTimezoneOffset());
    document.commitForm.submit();
}