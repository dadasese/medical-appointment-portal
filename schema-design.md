## MySQL Database Design
 CREATE TABLE `patient` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `lastName` VARCHAR(45) NULL,
  `username` VARCHAR(45) NULL,
  `password` VARCHAR(45) NULL,
  `email` VARCHAR(45) NULL,
  PRIMARY KEY (`id`));

  CREATE TABLE `doctor` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `lastName` VARCHAR(45) NULL,
  `username` VARCHAR(45) NULL,
  `password` VARCHAR(45) NULL,
  `email` VARCHAR(45) NULL,
  PRIMARY KEY (`id`));

  CREATE TABLE `admin` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `lastName` VARCHAR(45) NULL,
  `username` VARCHAR(45) NULL,
  `password` VARCHAR(45) NULL,
  `email` VARCHAR(45) NULL,
  PRIMARY KEY (`id`));

  CREATE TABLE `appointment` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `doctor_id` INT NULL,
  `patient_id` INT NULL,
  `appointment_time` DATE NULL,
  `status` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `IDX_PATIENT` (`patient_id` ASC) VISIBLE,
  INDEX `IDX_DOCTOR` (`doctor_id` ASC) VISIBLE,
  CONSTRAINT `fk_health_history_patient_id`
    FOREIGN KEY (`patient_id`)
    REFERENCES `patient` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_health_history_doctor_id`
    FOREIGN KEY (`doctor_id`)
    REFERENCES `doctor` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

 CREATE TABLE `health_history` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `appointment_id` INT NULL,
  `description` VARCHAR(45) NULL,
  `creation_date` DATE NULL,
  PRIMARY KEY (`id`),
  INDEX `IDX_APPOINTMENT` (`appointment_id` ASC) VISIBLE,
  CONSTRAINT `fk_health_history_appointment_id`
    FOREIGN KEY (`appointment_id`)
    REFERENCES `appointment` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

## MongoDB Collection Design
### Collection: prescriptions
```json
{
  "_id": "ObjectId('29e123keqdas')",
  "appointmentId": 45,
  "medication": [
  {
    "name": "Dozil",
    "dosage": "50mg",
    "doctorNotes": "Take 1 pill per day",
    "refillCount": 1
   },
  {
    "name": "Morphine",
    "dosage": "50mg",
    "doctorNotes": "Take 1 shot per day",
    "refillCount": 50
   }
  ],
  "expiration_date": "2025-05-03",
  "pharmacy": {
    "name": "Green Market SF",
    "location": "Big ATM Mall"
  },
  "tags": ["Dozil","Morphine","Green Market SF","2025-05"],
  "metadata": {
    "createdAt": ISODate("2025-06-10T02:45:30Z"),
    "createdBy": "preescription-service",
    "updatedAt": null,
    "updateBy": null
  }
}
```

### Collection: feedback
```json
{
  "_id": "ObjectId('3v5s523vrw83gd')",
  "appointmentId": 45,
  "review_appointment": "The doctor seems confusing and unsure",
  "concrete_review": "Good",
  "tags": ["Good"],
  "metadata": {
    "createdAt": ISODate("2025-07-10T02:45:30Z"),
    "createdBy": "feedback-service",
    "updatedAt": null,
    "updateBy": null
  }
}
```

### Collection: log
```json
{
  "_id": "ObjectId('3v5s523vrw83gd')",
  "description": "Failed to connect to create service: timeout",
  "status": "failed",
  "tags": ["failed","database","service"],
  "metadata": {
    "createdAt": ISODate("2025-08-10T02:45:30Z"),
    "createdBy": "log-service",
    "updatedAt": null,
    "updateBy": null
  }
}
```
