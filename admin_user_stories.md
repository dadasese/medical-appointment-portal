**Security admin login**
As a portal administrator, I want to log in with my username and password, so that I access and manage the platform securely
**Acceptance Criteria:**
1. Successful login
   **Given** I'm on the Login page
   **When** I enter a valid username and password
   **And** I click Login
   **Then** I am redirected to the Admin Dashboard
   **And** I see a welcome message
2. Required fields enforcement
   **Given** I'm on the Login page
   **When** I leave username and/or password blank
   **And** I click Login
   **Then** I remain on the Login page
   **And** I see inline errors for each missing field("Username is required.","Password is required")

**Priority:** High
**Story Points:** 1

**Security admin logout**
As a portal administrator, I want to log out of the portal, so that I protect system access when I'm done
**Acceptance Criteria:**
1. Manual log out
   **Given** I'm authenticated an my session is active
   **When** I click log out button
   **Then** My session is terminated on the server
   **And** Any authentication tokens/cookies are cleared from the browser
   **And** I am redirected to the Login page
   **And** I see a confirmation message: "You have successfully logged out"
   
**Priority:** High
**Story Points:** 1

**Add Doctor to portal**
As a portal administrator, I want to add new doctors into the system, so that they can log in, attend to patients, and manage their schedules
**Acceptance Criteria:**
1. Successful registration
   **Given** I'm on the Add Doctor form
   **When** I enter valid values for all mandatory fields
   **Then** the form fields are cleared
   **And** I see a success notification: "Doctor added successfully"
   **And** the new doctor appears in the Doctors List
3. Required fields enforcement
   **Given** I'm on the Add Doctor form
   **When** I leave one or more mandatory fields blank
   **And** I click 'save' button
   **Then** I remain in the Add Doctor form
   **And** I see inline error messages for each missing field
4. Cancel button
   **Given** I'm on the Add Doctor form
   **When** I click Cancel
   **And** I click 'save' button
   **Then** I am navigated back to the Doctors List
   **And** no new doctor is created
   
**Priority:** High
**Story Points:** 1

**Remove Doctor from portal**
As a portal administrator, I want to delete doctor's profile from the system, so that they cannot log in if they no longer work at the hospital
**Acceptance Criteria:**
1. Manual deletion flow
   **Given** I'm on Doctors List page
   **When** I selected an existing doctor and click Delete
   **Then** a modal dialog appears with:
   - "Are you sure you want to delete Doctor <fullName>?"
   - Confirm and Cancel buttons
2. Confirm delete
   **Given** the confirmation model is visible
   **When** I click Confirm
   **Then** the doctor's profile is permanently removed from the database
   **And** the list refreshes showing no entry for that doctor
   **And** see a toast/alert: "Doctor removed successfully"

**Priority:** High
**Story Points:** 1

**View Appointment Statistics via CLI**
As a portal administrator, I want to retrieve the number of appointments per month using a stored procedure in the MySQL CLI, so that I can make informed decisions about server capacity and hospital administration
**Acceptance Criteria:**
1. Get the number of appointments per month
   **Given** I'm connected to the MySQL server via CLI
   **When** I list procedures in the hospital database
   **Then** I see a procedure named sp_get_monthly_appointments
2. Procedure input parameters
   **Given** I'm about to call the procedure
   **When** I run "CALL sp_get_monthly_appointments('2025-01-01', '2025-06-30');"
   **Then** the procedure accepts two DATE parameters: start_date and end_date.
   
**Priority:** High
**Story Points:** 1
