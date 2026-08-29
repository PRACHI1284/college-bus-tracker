const express = require('express');
const cors = require('cors');

const app = express();
const port = 3000;

app.use(cors());
app.use(express.json());

// Mock Data for Campus Routes
const mockRoutes = [
    {
        id: "ROUTE_NORTH",
        name: "North Campus Loop",
        description: "Connects Library, Science Block, and North Hostels",
        stops: ["Main Gate", "Library", "Science Block", "North Hostels", "Sports Complex"],
        schedule: ["08:00 AM", "09:00 AM", "10:00 AM", "11:00 AM"],
        pathPoints: [
            { first: 0.5, second: 0.8 },
            { first: 0.5, second: 0.5 },
            { first: 0.3, second: 0.3 },
            { first: 0.2, second: 0.2 }
        ]
    },
    {
        id: "ROUTE_SOUTH",
        name: "South Campus Express",
        description: "Direct connection to Engineering and South Hostels",
        stops: ["Main Gate", "Engineering Dept", "Cafeteria", "South Hostels"],
        schedule: ["08:15 AM", "09:15 AM", "10:15 AM", "11:15 AM"],
        pathPoints: [
            { first: 0.5, second: 0.8 },
            { first: 0.6, second: 0.6 },
            { first: 0.7, second: 0.4 },
            { first: 0.8, second: 0.2 }
        ]
    }
];

// Mock Data for Live Buses
const mockLiveBuses = [
    {
        id: "BUS-101",
        driverName: "John Smith",
        driverPhone: "+1234567890",
        routeId: "ROUTE_NORTH",
        routeName: "North Campus Loop",
        status: "On Time",
        delayMinutes: 0,
        capacity: 40,
        currentPassengers: 12,
        latitudePercent: 0.5,
        longitudePercent: 0.65,
        nextStop: "Library",
        etaMinutes: 3
    },
    {
        id: "BUS-205",
        driverName: "Sarah Connor",
        driverPhone: "+1987654321",
        routeId: "ROUTE_SOUTH",
        routeName: "South Campus Express",
        status: "Delayed",
        delayMinutes: 5,
        capacity: 40,
        currentPassengers: 35,
        latitudePercent: 0.6,
        longitudePercent: 0.6,
        nextStop: "Engineering Dept",
        etaMinutes: 8
    }
];

// Routes Endpoint
app.get('/api/v1/routes', (req, res) => {
    res.json(mockRoutes);
});

// Live Buses Endpoint
app.get('/api/v1/buses/live', (req, res) => {
    // In a real app, you might update positions randomly here before sending
    res.json(mockLiveBuses);
});

// Specific Bus Endpoint
app.get('/api/v1/buses/:id', (req, res) => {
    const bus = mockLiveBuses.find(b => b.id === req.params.id);
    if (bus) {
        res.json(bus);
    } else {
        res.status(404).json({ message: "Bus not found" });
    }
});

// Search Buses on a Route
app.get('/api/v1/buses/search', (req, res) => {
    const routeId = req.query.routeId;
    const buses = mockLiveBuses.filter(b => b.routeId === routeId);
    res.json(buses);
});

// Start Server
app.listen(port, () => {
    console.log(`🚌 College Bus Tracker Backend running at http://localhost:${port}`);
});
