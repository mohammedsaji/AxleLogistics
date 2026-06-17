async function ajaxCall(url, methodType, content) {
    const options = {
        method: methodType,
        credentials: 'include'
    };

    if ((methodType === 'POST' || methodType === 'PUT') && content != null) {
        options.headers = { 'Content-Type': 'application/json' };
        options.body = JSON.stringify(content);
    }

    try {
        const response = await fetch(url, options);
        if (response.status === 204) return response.ok ? "Success" : null;

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