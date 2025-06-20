**Doctor Login:**
As a Doctor, I want to log in to the portal, so that I can securely manage my appointments.
**Acceptance Criteria:**
1. Login page exists
   **Given** I'm not authenticated
   **when** I navigate to login
   **then** I see fields for email/username and password.
2. Successful login
   **Given** valid credentials
   **When** I click 'Log In'
   **then** I'm redirected to my dashboard.
   **and** I see a welcome message with my name
**Priority:** High
**Story Points:** 1

**Doctor Log out:**
As a Doctor, I want to log out of the portal, so that I can protect my patient data
**Acceptance Criteria:**
1. Logout button/link
   **Given** I'm logged in
   **when** I look at the header or menu
   **then** I see a "Log Out" option
2. Successful logout
   **Given** I click 'Log Out'
   **then** my session is terminated
   **and** I'm redirected to the public landing or login page
**Priority:** High
**Story Points:**  1

**View Appointment Calendar:**
As a Doctor, I want to view my appointment calendar, so that I can stay organized and see my day/week/month at a glance
**Acceptance Criteria:**
1. Calendar view
   **Given** I'm on my dashboard
   **when** I click 'Appointments'/'My Calendar'
   **then** I see a calendar grid(day/week/month)
2. Upcoming appointments
   **Given** I have appointments scheduled
   **then** I look at the calendar
   **and** each appointment appears in the correct time slot with patient name and type
**Priority:** High
**Story Points:**  1

**Mark Unavailability**
As a Doctor, I want to mark time slots as unavailable so that patients can only book during my free slots
**Acceptance Criteria:**
1. Unavailability UI
   On the calendar, each slot has an "Add Unavailability" action.
2. Blocking slots
   **Given** I salect a time range and click "Mark Unavailable",
   **then** those slots become gray-out(unbookable)
**Priority:** High
**Story Points:**  1

**Update Profile**
As a Doctor, I want to update my profile with my specialization and contact info, so that patients see up-to-date details
before booking
**Acceptance Criteria:**
1. Profile page
   **Given** I'm logged in
   **when** I click 'my Profile'
   **then** I see editable fields
2. Save changes
   **Given** valid inputs
   **when** I click 'Save'
   **then** I see success message
**Priority:** High
**Story Points:**  1

**View Patient Details**
As a Doctor, I want to view patient details for upcoming appointments, so that I can prepare for each consultation
**Acceptance Criteria:**
1. Patient info modal/page
   **When** I click 'View Patient', a modal (or new page) shows with patient information
**Priority:** High
**Story Points:**  1
