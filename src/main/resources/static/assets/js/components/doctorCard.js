import { getPatientData } from "../util.js";
import { showBookingOverlay } from "./modals.js";

export function createDoctorCard(doctor) {
    const card = document.createElement("div");
    card.classList.add("doctor-card");

    const role = localStorage.getItem("userRole");

    const infoDiv = document.createElement("div");
    infoDiv.classList.add("doctor-info");

    const name = document.createElement("h3");
    name.textContent = doctor.name;

    const specialization = document.createElement("p");
    specialization.textContent = `Specialty: ${doctor.specialization}`;

    const email = document.createElement("p");
    email.textContent = `Email: ${doctor.email}`;

    const availability = document.createElement("p");
    availability.textContent = `Available: ${doctor.availability.join(", ")}`;

    infoDiv.appendChild(name);
    infoDiv.appendChild(specialization);
    infoDiv.appendChild(email);
    infoDiv.appendChild(availability);

    const actionsDiv = document.createElement("div");
    actionsDiv.classList.add("card-actions");

    if (role === "admin") {
        const removeBtn = document.createElement("button");
        removeBtn.textContent = "Delete";
        removeBtn.addEventListener("click", async () => {
            const confirmDelete = confirm("Are you sure you want to delete this doctor?");
            if (!confirmDelete) return;
            const token = localStorage.getItem("token");

            try {
                const response = await fetch(`/api/doctors/${doctor.id}`, {
                    method: "DELETE",
                    headers: { Authorization: `Bearer ${token}` },
                });

                if (response.ok) {
                    card.remove();
                } else {
                    alert("Failed to delete doctor.");
                }
            } catch (error) {
                console.error("Error deleting doctor:", error);
                alert("An error occurred.");
            }
        });
        actionsDiv.appendChild(removeBtn);

    } else if (role === "patient") {
        const bookNow = document.createElement("button");
        bookNow.textContent = "Book Now";
        bookNow.addEventListener("click", () => {
            alert("Please log in to book an appointment.");
        });
        actionsDiv.appendChild(bookNow);

    } else if (role === "loggedPatient") {
        const bookNow = document.createElement("button");
        bookNow.textContent = "Book Now";
        bookNow.addEventListener("click", async (e) => {
            const token = localStorage.getItem("token");
            const patientData = await getPatientData(token);
            showBookingOverlay(e, doctor, patientData);
        });
        actionsDiv.appendChild(bookNow);
    }

    card.appendChild(infoDiv);
    card.appendChild(actionsDiv);

    return card;
}