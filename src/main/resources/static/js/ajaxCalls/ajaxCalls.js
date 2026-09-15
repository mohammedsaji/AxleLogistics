function getCookie(name) {
    const nameEQ = name + "=";
    const ca = document.cookie.split(';');
    for (let i = 0; i < ca.length; i++) {
        let c = ca[i].trim();
        if (c.indexOf(nameEQ) === 0) {
            return decodeURIComponent(c.substring(nameEQ.length, c.length));
        }
    }
    return null;
}

async function ajaxCall(url, methodType, content) {
    const options = {
        method: methodType,
        credentials: 'include',
        headers: {
            'Username': getCookie('username'),
            'X-API-KEY': getCookie('X-API-KEY'),
            'AUTH-TOKEN': getCookie('AUTH-TOKEN'),
            'X-CSRF-TOKEN': getCookie('X-CSRF-TOKEN')
        }
    };

    if ((methodType === 'POST' || methodType === 'PUT') && content != null) {
        options.headers = { ...options.headers,'Content-Type': 'application/json' };
        options.body = JSON.stringify(content);
    }

    try {
        const response = await fetch(url, options);

        if (response.status === 204) {
            return { success: true, data: null, message: "Operation completed." };
        }

        const contentType = response.headers.get("content-type") || "";
        if (contentType.includes("application/json")) {
            const data = await response.json();
            if (!response.ok) {
                console.error(`Error [${data.status || response.status}]: ${data.message}`);
                alert(`Error: ${data.message || "Server Error"}`);
                return null;
            }
            return data;
        }
        return await response.text();
    } catch (networkError) {
        console.error("Network failure:", networkError);
        alert("Network Error: Could not connect to the server.");
        return null;
    }

}