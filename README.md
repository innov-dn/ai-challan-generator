# 🚦 AI Challan Generator

A full-stack web application for generating
Indian traffic challans using AI.

## Tech Stack
- **Backend:** Java 17, Spring Boot 3.2, MongoDB
- **AI:** Google Gemini API (LangChain4j)
- **Frontend:** React (Vite), Axios
- **PDF:** iText 7
- **Law:** Motor Vehicles Act 1988 (Amended 2019)

## Features
- AI-generated challans using Gemini 3.1 Flash Lite
- India-specific MV Act sections & fine amounts
- PDF challan download
- PIN-protected access
- Vehicle number search
- Mark as Paid / Cancelled

## API Endpoints
- POST /api/challan/generate
- GET  /api/challan/all
- GET  /api/challan/vehicle/{vehicleNumber}
- PUT  /api/challan/{id}/status
- GET  /api/challan/{id}/pdf

## Setup
1. Clone the repo
2. Add Gemini API key in application.properties
3. Start MongoDB
4. Run Spring Boot app
5. Start React frontend
