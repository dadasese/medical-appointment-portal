import {API_BASE_URL} from "../config/config.js";

const PATIENT_API = API_BASE_URL +  '/patient';

// Handle patient signup
export async function patientSignup(data){
    try{
        const response = await fetch(`${PATIENT_API}/signup`,{
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            });
        const result = await response.json();
        return { success: response.ok, message: result.message };
    } catch(error){
        console.error("Signup error: ", error);
        return { success: false, message: "Signup failed. Please try again."}
    }
}

// Handle patient login
export async function patientLogin(data){
    try{
        return await fetch(`${PATIENT_API}/login`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(data)
        });
    } catch(error){
        console.error("Login error:", error);
    }
}

// Fetch logged-in patient data
export async function getPatientData(token) {
    try{
        const response = await fetch(`${PATIENT_API}/details`,{
            headers: { Authorization: `Bearer ${token}`}
        });
        if(!response.ok) throw new Error("Failed to fetch patient data.");
        return await response.json();
    } catch(error){
        console.error("Get patient data error: ", error)
        return null;
    }
}

// Fetch patient appointments
export async function getPatientAppointments(id, token, user){
    try{
        const response = await fetch(`${PATIENT_API}/appointments/${user}/${id}`,{
            headers: { Authorization: `Bearer ${token}`}
        });
        if(!response.ok) throw new Error("Failed to fetch appointments.");
        return await response.json();
    } catch(error){
        console.error("Appointment fetch error: ", error)
    }
}

//Filter appointments
export async function filterAppointments(condition, name, token){
    try {
        const response = await fetch(`${PATIENT_API}/appointments/filter/${condition}/${name}`,{
            headers: { Authorization: `Bearer ${token}`}
        });
        if(!response.ok) throw new Error("Filtering failed.");
        return await response.json();
    } catch(error){
        console.error("Filter error:", error);
        alert("Unable to filter appointments. Please try again");
        return [];
    }

}
