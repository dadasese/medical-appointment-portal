import { getAllAppointments } from "./services/appointmentRecordService.js";
import { createPatientRow } from "./components/patientRows.js"

const appointmentTableBody = document.getElementById("patientTableBody");
let selectedDate = new Date.toISOString().split("T")[0];
let token = localStorage.getItem("token");
let patientName = null;

const searchBar = document.getElementById("searchBar");
if (searchBar) {
    searchBar.addEventListener("input", () => {
        patientName = searchBar.value.trim() || "null";
        loadAppointments();
    });
}

const todayButton = document.getElementById("todayButton");
if (todayButton) {
    todayButton.addEventListener("click", () => {
        selectedDate = new Date().toISOString().split("T")[0];
        const datePicker = document.getElementById("datePicker");
        if (datePicker) datePicker.value = selectedDate;
        loadAppointments();
    })
}

const datePicker = document.getElementById("datePicker");
if (datePicker){
    datePicker.value = selectedDate;
    datePicker.addEventListener("change", () => {
        selectedDate = datePicker.value;
        loadAppointments();
    })
}

async function loadAppointments(){
    try{
        const appointments = await getAllAppointments(selectedDate, patientName, token);

        appointmentTableBody.innerHTML = "";

        if(!appointments || appointments.lenth === 0){
            const noRow = document.createElement("tr");
            noRow.innerHTML = `
            <td colspan="5" class="noPatientRecord">No Appointments found for selected date</td>
            `;
            appointmentTableBody.appendChild(noRow);
            return;
        }

        appointments.forEach((appointment) => {
            const row = createPatientRow(appointment);
            appointmentTableBody.appendChild(row);
        });
    } catch (error) {
        appointmentTableBody.innerHTML = `
              <tr><td colspan="5" class="noPatientRecord">Error loading appointments. Please try again.</td></tr>
            `;
                console.error("Error fetching appointments:", error);
    }

    // Initial render on page load
    document.addEventListener("DOMContentLoaded", () => {
        if (typeof renderContent === "function") renderContent();
        loadAppointments();
    });
}