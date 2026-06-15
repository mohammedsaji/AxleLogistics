
async function ajaxCall(url, methodType, content) {

    const options = {
        method: methodType,
        credentials: 'include'
    }

    if ((methodType === 'POST' || methodType === 'PUT') && content != null) {
        options.headers = {
            'Content-Type': 'application/json'
        }

        options.body = JSON.stringify(content);
    }

    try{
        const response = await fetch(url, options);
        //{
        // method  : methodType,
        // headers : {
        //     'Content-Type': 'application/json'
        // },
        // body    : JSON.stringify(content)
        //});

        if (response.status === 204) {
            return response.ok ? "Success" : null;
        }

        let contentType =  response.headers.get("content-type") || "";

        if(contentType.includes("text/plain")){
            if(response.ok){
                return await response.text();
            }else{
                const rawTextResponse = await response.text();
                console.error("Raw Backend Error Text:", rawTextResponse);
                alert(`Server Error (${response.status}): ${rawTextResponse || 'Unknown Error'}`);
            }
        }else if(contentType.includes("application/json")){

            if(response.ok) {
                return await response.json();
            }else{
                const errorResponse = await response.json();

                const status = errorResponse.status || response.status;
                const message = errorResponse.message || "A backend server error occurred.";
                const errorType = errorResponse.error || "Bad Request";

                console.error(`Status: ${status} | Error: ${errorType}`);
                console.error(`Message: ${message}`);

                alert(`Error [${status}]: ${message}`);

            }
        }else {
            if (!response.ok) {
                const rawFallback = await response.text();
                alert(`Server Error (${response.status}): ${rawFallback || 'Unknown Error'}`);
            }
        }

        return null;
    }catch(networkError){
        console.error("Network connection failure:", networkError);
        alert("Network Error: Could not connect to the logistics server. Please check your connection.");
        return null;
    }
}
