function getCurrentGeoLocation() {
    return new Promise((resolve, reject) => {

        if (!navigator.geolocation) {
            reject(new Error("Geo location is not supported by browser which is currently in use."));
            return;
        }

        navigator.geolocation.getCurrentPosition(position => {
            resolve({
                latitude: position.coords.latitude,
                longitude: position.coords.longitude,
                accuracy: position.coords.accuracy
            });
        },error =>{
            reject(error);
        },{
            enableHighAccuracy : true,
            timeout : 10000,
            maximumAge : 0
        });
    });
}

function reverseGeocode(latitude, longitude) {
    return new Promise(async (resolve, reject) => {
        if (!latitude || !longitude) {
            reject(new Error("Latitude and Longitude are required for reverse geocoding."));
            return;
        }

        const url = `https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=${latitude}&lon=${longitude}`;

        try {
            const response = await fetch(url, {
                headers: {
                    'Accept-Language': 'en'
                }
            });

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const data = await response.json();

            if (data && data.address) {
                const addr = data.address;

                // Formats city/town/village dynamically depending on what OSM returns
                const placeName = addr.amenity || addr.building || addr.road;
                const city = addr.city || addr.town || addr.village || addr.suburb || addr.county;
                const state = addr.state;
                const country = addr.country;

                const locationString = [placeName, city, state, country]
                    .filter(Boolean)
                    .join(', ');

                resolve(locationString || data.display_name);
            } else {
                reject(new Error("No address found for the given coordinates."));
            }
        } catch (error) {
            reject(error);
        }
    });
}