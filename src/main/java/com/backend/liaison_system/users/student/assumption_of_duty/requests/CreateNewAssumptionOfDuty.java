package com.backend.liaison_system.users.student.assumption_of_duty.requests;

public record CreateNewAssumptionOfDuty(
        String companyName,
        String companyPhone,

        String companyExactLocation,
        String companyTown,
        String companyRegion,

        String companyAddress,
        String companyEmail,

        String companySupervisor,
        String supervisorPhone,

        String letterTo,

        String companyLongitude,
        String companyLatitude
) { }
