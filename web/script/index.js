'use strict';

const SECTIONS = ['index', 'masters', 'contacts', 'services', 'news', 'login', 'regin'];
const CONTROLLERS = ['index', 'services', 'masters', 'news'];
let user;
let isServicesDownloaded = false;
let isNewsDownloaded = false;

async function initPage() {
    const response = await fetch('index');
    if(response.ok){
        try{
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
    let hrefHash = (window.location.hash).replace("#","");
    if(SECTIONS.includes(hrefHash)){
        showSection(hrefHash);
    }else{
        showSection('index');
    }
}
async function initServices(){
    if(!isServicesDownloaded){
        const response = await fetch('booking?kind=service&choice=0');
        if(response.ok){
            try{
                const json = await response.json();
                const serviceMap = json.services;
                printServices(serviceMap);
            } catch (e) {
                console.error("Server not response: " + e.message);
            }
        } else {
            console.error("HTTP error: " + response.status);
        }
        isServicesDownloaded = true;
    }
    showSection('services');
}
async function initNews(){
    showSection('news');

}
async function doRegin(){
    document.reginForm.setAttribute('action', 'regin?timezone='+new Date().getTimezoneOffset());
    document.reginForm.submit();
    const response = await fetch('regin');
    if(response.ok){
        try{
            const json = await response.json();
            user = json.user;
            const error = json.error;
            if(user.name == "" && error != ""){
                document.getElementById('reginError').innerHTML = error;
                return;
            }
            showSection('index');
        } catch (e) {
            console.error("Server not response: " + e.message);
        }
    } else {
        console.error("HTTP error: " + response.status);
    }
}
async function doLogin() {
    document.loginForm.submit();
    const response = await fetch('login');
    if(response.ok){
        try{
            const json = await response.json();
            user = json.user;
            const error = json.error;
            if(user.name == "" && error != ""){
                document.getElementById('loginError').innerHTML = error;
                return;
            }
            showSection('index');
        } catch (e) {
            console.error("Server not response: " + e.message);
        }
    } else {
        console.error("HTTP error: " + response.status);
    }
}

function showSection(sectionName) {
    for (let sec of SECTIONS) {
        document.getElementById(`${sec}`).style.display = 'none';
    }
    document.getElementById(`${sectionName}`).style.display = 'block';
}
function initUserButtons() {
    let isShowForGuest = isGuest();
    let guestBtns = document.getElementsByClassName("guestBtn");
    let userBtns = document.getElementsByClassName("userBtn");
    for(let btn of guestBtns){
        btn.style.display = isShowForGuest ? 'block' : "none";
        document.getElementById("userName").innerHTML = "";
    }
    for(let btn of userBtns){
        btn.style.display = isShowForGuest ? 'none' : "block";
        document.getElementById("userName").innerHTML = user.name;
    }
}
function isGuest() {
    return user.id <= 0 || user.name == ""
        || user.name == null || user.status == "Guest"
}

function onClickCategory(categoryId){
    document.getElementById(categoryId).classList.toggle('open');
}
function printServices(serviceMap) {
    const serviceTable = document.getElementById('serviceTable');
    const getCatMenu = function (categoryName) {
        const div = document.createElement('div');
        div.setAttribute('class', 'category-menu');
        div.setAttribute('id', categoryName);
        const span = document.createElement('span');
        span.setAttribute('class', 'category-title unit-full');
        span.setAttribute('onclick', `onClickCategory('${categoryName}')`);
        const h3 = document.createElement('h3');
        h3.innerHTML = categoryName.toUpperCase();
        span.appendChild(h3);
        div.appendChild(span);
        return div;
    }
    const getElemOfList = function (serviceName, serviceTime, serviceCost) {
        const name = document.createElement('h3');
        const time = document.createElement('h5');
        const cost = document.createElement('h4');
        name.innerHTML = serviceName;
        time.innerHTML = serviceTime;
        cost.innerHTML = serviceCost;
        const getUnit = function (className, elem) {
            const div = document.createElement('div');
            div.setAttribute('class', className);
            div.appendChild(elem);
            return div;
        }
        const li = document.createElement('li');
        li.appendChild(getUnit('unit-2of4', name));
        li.appendChild(getUnit('unit-1of4', time));
        li.appendChild(getUnit('unit-1of4', cost));
        return li;
    }
    for(let cat of serviceMap){
        const catName = cat.category;
        const catArr = cat.services;
        const div = getCatMenu(catName);
        const ul = document.createElement('ul');
        for(let serv of catArr){
            if(serv != null){
                ul.appendChild(getElemOfList(serv.name, `от ${serv.time} мин.`, `${serv.cost} руб.`));
            }
        }
        div.appendChild(ul);
        serviceTable.appendChild(div);
    }
}

