import { openModal } from "/components/modals.js";
import { getDoctors, filterDoctors, saveDoctor } from "/services/doctorServices.js";
import { createDoctorCard } from "components/doctorCard.js";

// Load doctor cards on page load
window.addEventListener("DOMContentLoaded", loadDoctorCards);

// Event: Add Doctor button
const addDocBtn = document.getElementById("addDocBtn");
if (addDocBtn) {
    addDocBtn.addEventListener("click", () => openModal("addDoctor"));
}

// Event: Filter/Search
const searchBar = document.getElementById("searchBar");
const filterTime = document.getElementById("filterTime");
const filterSpecialty = document.getElementById("filterSpecialty");

if (searchBar) searchBar.addEventListener("input", filterDoctorsOnChange);
if (filterTime) filterTime.addEventListener("change", filterDoctorsOnChange);
if (filterSpecialty) filterSpecialty.addEventListener("change", filterDoctorsOnChange);

// Load and render all doctor cards
async function loadDoctorCards(){
    const doctors = await getDoctors();
    renderDoctorCards(doctors);
}

// Render list of doctor cards
function renderDoctorCards(doctors){
    const contentDiv = document.getElementById("content");
    contentDiv.innerHTML = "";

    if(!doctors || doctors.length === 0){
        contentDiv.innerHTML = "<p>No doctors found.<p>";
        return;
    }

    doctors.forEach((doctor) => {
        const card = createDoctorCard(doctor);
        contentDiv.appendChild(card);
    });
}

// Handle form submission for adding a doctor
export async function adminAddDoctor(event){
    event.preventDefault();

    const token = localStorage.getItem("token");
    if(!token){
        alert("Unauthorized. Please log in again");
        return;
    }

    const name = document.getElementById("doctorName").value;
    const email = document.getElementById("doctorEmail").value;
    const password = document.getElementById("doctorPassword").value;
    const phone = document.getElementById("doctorPhone").value;
    const specialty = document.getElementById("doctorSpecialty").value;
    const timeCheckboxes = document.querySelectorAll("input[name='availability']");

    const availability = Array.from(timeCheckboxes).map((cb) => cb.value);

    const doctor = {name, email, password, phone, specialty, availability};

    try{
        const response = await saveDoctor(doctor, token);

        if(response.success) {
            alert("Doctor added successfully.");
            document.getElementById("model").style.disply = "none";
            loadDoctorCards();
        } else {
            alert("Failed to add doctor: " + response.message);
        }
    } catch (error) {
        console.error("Add doctor error: " + error);
        alert("Something went wrong while adding the doctor.");
    }
}