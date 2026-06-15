function shipmentViewState(){
    return  {
        "SHIPMENT":{
            "CREATE":"SHIPMENT_RECORD_CREATE",
            "READ":"SHIPMENT_RECORD_READ"
        },
        "CARGO":{
            "READ": "SHIPMENT_CARGO_READ"
        },
        "CUSTOMER":{
            "READ":"SHIPMENT_CARGO_CUSTOMER_READ"
        }
    }
}

function operatorViewState(){
    return {
        "OPERATOR": {
            "CREATE": "OPERATOR_PROFILE_CREATE",
            "READ": "OPERATOR_PROFILE_READ",
        }
    };
}
//
// function vehicleViewState(){
//     return {
//         "VEHICLE": {
//             "CREATE": "OPERATOR_VEHICLE_CREATE",
//             //"READ": "OPERATOR_VEHICLE_READ"
//         }
//     };
// }

function federateViewState(){
    return {
        "FEDERATE_ACC": {
            "CREATE": "FEDERATE_ACCOUNT_CREATE"
        }
    }
}

function managerViewState() {
    return {
        "MANAGER_PROFILE": {
            "CREATE": "MANAGER_PROFILE_CREATE",
            //"READ": "MANAGER_PROFILE_READ"
        }
    };
}

function driverViewState() {
    return {
        "DRIVER_PROFILE": {
            "CREATE": "DRIVER_PROFILE_CREATE",
            //"READ": "DRIVER_PROFILE_READ"
        }
    };
}

function employeeViewState() {
    return {
        "EMPLOYEE_ACCOUNT": {
            "CREATE": "INTERNAL_EMPLOYEE_ACCOUNT_CREATE",
            "READ":"INTERNAL_EMPLOYEE_ACCOUNT_READ",
            "ADMINSTR":"INTERNAL_EMPLOYEE_ACCOUNT_ADMINSTR"
        }
    };
}

function statusViewState() {
    return {
        "SHIPMENT_STATUS": {
            "READ": "SHIPMENT_STATUS_READ"
        },
        "SHIPMENT_ASSIGNMENT": {
            "DRIVER_REASSIGN": "SHIPMENT_DRIVER_REASSIGN",
            "VEHICLE_REASSIGN": "SHIPMENT_VEHICLE_REASSIGN",
            "OPERATOR_REASSIGN": "SHIPMENT_OPERATOR_REASSIGN"
        }
    };
}

function transportTypeViewState(){
    return {
        "TYPE": "TRANSPORT_TYPE"
    }
}

function dashboardViewState(){
    return {
        "DASHBOARD":"MAIN_MENU"
    }
}