// header.js

function renderHeader() {
    const headerDiv = document.getElementById("header");
    if (!headerDiv) return;

    // Clear session on homepage
    if (window.location.pathname.endsWith("/")) {
        localStorage.removeItem("userRole");
        localStorage.removeItem("token");
    }

    const role = localStorage.getItem("userRole");
    const token = localStorage.getItem("token");

    // Invalid session check
    if ((role === "loggedPatient" || role === "admin" || role === "doctor") && !token) {
        localStorage.removeItem("userRole");
        alert("Session expired or invalid login. Please log in again.");
        window.location.href = "/";
        return;
    }

    let headerContent = "<nav class='header-nav'>";

    if (role === "admin") {
        headerContent += `
      <button id="addDocBtn" class="adminBtn" onclick="openModal('addDoctor')">Add Doctor</button>
      <a href="#" id="logoutBtn">Logout</a>`;
    } else if (role === "doctor") {
        headerContent += `
      <a href="/templates/doctor/doctorDashboard.html" id="homeBtn">Home</a>
      <a href="#" id="logoutBtn">Logout</a>`;
    } else if (role === "patient") {
        headerContent += `
      <a href="/login.html" id="loginBtn">Login</a>
      <a href="/signup.html" id="signupBtn">Sign Up</a>`;
    } else if (role === "loggedPatient") {
        headerContent += `
      <a href="/patientDashboard.html" id="homeBtn">Home</a>
      <a href="/appointments.html" id="appointmentsBtn">Appointments</a>
      <a href="#" id="logoutPatientBtn">Logout</a>`;
    }

    headerContent += "</nav>";
    headerDiv.innerHTML = headerContent;

    attachHeaderButtonListeners();
}

function attachHeaderButtonListeners() {
    const logoutBtn = document.getElementById("logoutBtn");
    if (logoutBtn) {
        logoutBtn.addEventListener("click", logout);
    }

    const logoutPatientBtn = document.getElementById("logoutPatientBtn");
    if (logoutPatientBtn) {
        logoutPatientBtn.addEventListener("click", logoutPatient);
    }

    const addDocBtn = document.getElementById("addDocBtn");
    if (addDocBtn) {
        addDocBtn.addEventListener("click", () => openModal("addDoctor"));
    }
}

function logout() {
    localStorage.removeItem("userRole");
    localStorage.removeItem("token");
    window.location.href = "/";
}

function logoutPatient() {
    localStorage.setItem("userRole", "patient");
    localStorage.removeItem("token");
    window.location.href = "/patientDashboard.html";
}

// Call renderHeader on page load
renderHeader();