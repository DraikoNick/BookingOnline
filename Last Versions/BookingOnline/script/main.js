'use strict';

const SECTIONS = ['welcome', 'contacts', 'about', 'login', 'regin'];

function showSection(sectionName) {
    for (let sec of SECTIONS) {
        document.getElementById(`${sec}`).style.display = 'none';
    }
    document.getElementById(`${sectionName}`).style.display = 'block';
}