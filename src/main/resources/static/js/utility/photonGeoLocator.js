async function searchLocation(query) {

    if (query.length < 3) {
        return [];
    }

    const url =
        `https://photon.komoot.io/api/?q=${encodeURIComponent(query)}&limit=5`;

    try {
        const response = await fetch(url);

        if (!response.ok) {
            throw new Error(`Photon request failed: ${response.status}`);
        }

        const data = await response.json();

        console.log(data.features);

        return data.features;

    } catch (error) {
        console.error("Location search failed:", error);
        return [];
    }
}