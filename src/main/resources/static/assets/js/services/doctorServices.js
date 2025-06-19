import {API_BASE_URL} from "../config/config.js"

const DOCTOR_API =  `${API_BASE_URL}/doctor`;

// Fetch all doctors
export async function getDoctors() {
    try{
        const response = await fetch(DOCTOR_API);
        if(!response.OK) throw new Error("Failed to fetch doctors");
        return await response.json();
    } catch(error){
        console.error("Error fetching doctors: ", error);
        return[];
    }
}
// Delete doctor by ID
export async function deleteDoctor(id, token){
    try{
        const response = await fetch(`${DOCTOR_API}/${id}`,{
            method: "DELETE",
            header: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            }
        });
        const result = await response.json();
        return {
            success: response.ok,
            message: result.message || "Doctor deleted successfully."
        }
    } catch(error){
        console.error("Error deleting doctor: ", error);
        return {
            success: false,
            message: "Failed to delete doctor."
        };
    }
}
// Save a new doctor
export async function saveDoctor(doctor, token){
    try{
        const response = await fetch(DOCTOR_API, {
            method: "POST",
            header: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            },
            body: JSON.stringify(doctor)
        });
        const result = await response.json();
        return{
            success: response.ok,
            message: result.message || "Doctor added successfully."
        }
    } catch(error){
        console.error("Error saving doctor: ", error);
        return {
            success: false,
            message: "Failed to save doctor."
        }
    }
}
// Filter doctors by name, time, and specialty
export async function filterDoctors(name, time, specialty){
    try{
        const query = new URLSearchParams();
        if (name) query.append("name", name);
        if (time) query.append("time", time);
        if (specialty) query.append("specialty", specialty);

        const response = await fetch(`${DOCTOR_API}/filter?${query.toString()}`);
        if(!response.ok) throw new Error("Failed to filter doctors");
        return await response.json();
    } catch(error){
        console.error("Error filtering doctors:", error);
        return [];
    }
}

