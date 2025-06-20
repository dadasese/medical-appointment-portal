import { createDoctorCard } from "./components/doctorCard.js";
import { openModal } from "./components/modals.js";
import { getDoctors, filterDoctors } from "./services/doctorServices.js";
import { patientLogin, patientSignup } from "./services/patientServices.js";

// Load Doctor Cards on Page Load
document.addEventListener("DOMContentLoaded", () => {
    loadDoctorCards();

    const signupBtn = document.getElementById("patientSignup");
    if (signupBtn) signupBtn.addEventListener("click", () => openModal("patientSignup"));

    const loginBtn = document.getElementById("patientLogin");
    if (loginBtn) loginBtn.addEventListener("click", () => openModal("patientLogin"));

    document.getElementById("searchBar")?.addEventListener("input", filterDoctorsOnChange);
    document.getElementById("filterTime")?.addEventListener("change", filterDoctorsOnChange);
    document.getElementById("filterSpecialty")?.addEventListener("change", filterDoctorsOnChange);
});

async function loadDoctorCards() {
    try {
        const doctors = await getDoctors();
        renderDoctorCards(doctors);
    } catch (error) {
        console.error("Failed to load doctors:", error);
    }
}

function renderDoctorCards(doctors) {
    const contentDiv = document.getElementById("content");
    contentDiv.innerHTML = "";
    if (doctors.length > 0) {
        doctors.forEach((doctor) => {
            const card = createDoctorCard(doctor);
            contentDiv.appendChild(card);
        });
    } else {
        contentDiv.innerHTML = "<p>No doctors found.</p>";
    }
}

async function filterDoctorsOnChange() {
    const name = document.getElementById("searchBar")?.value || "";
    const time = document.getElementById("filterTime")?.value || "";
    const specialty = document.getElementById("filterSpecialty")?.value || "";

    try {
        const doctors = await filterDoctors(name, time, specialty);
        renderDoctorCards(doctors);
    } catch (error) {
        console.error("Error filtering doctors:", error);
        document.getElementById("content").innerHTML = "<p>Error loading doctors. Please try again later.</p>";
    }
}

window.signupPatient = async function () {
    const name = document.getElementById("signupName")?.value;
    const email = document.getElementById("signupEmail")?.value;
    const password = document.getElementById("signupPassword")?.value;
    const phone = document.getElementById("signupPhone")?.value;
    const address = document.getElementById("signupAddress")?.value;

    const data = { name, email, password, phone, address };

    try {
        const response = await patientSignup(data);
        if (response.success) {
            alert(response.message);
            document.getElementById("modal").style.display = "none";
            location.reload();
        } else {
            alert(response.message || "Signup failed.");
        }
    } catch (error) {
        console.error("Signup error:", error);
        alert("An error occurred during signup.");
    }
};

window.loginPatient = async function () {
    const email = document.getElementById("loginEmail")?.value;
    const password = document.getElementById("loginPassword")?.value;

    const data = { email, password };

    try {
        const response = await patientLogin(data);
        if (response.ok) {
            const json = await response.json();
            localStorage.setItem("token", json.token);
            localStorage.setItem("userRole", "loggedPatient");
            window.location.href = "loggedPatientDashboard.html";
        } else {
            alert("Invalid credentials.");
        }
    } catch (error) {
        console.error("Login error:", error);
        alert("An error occurred during login.");
    }
};