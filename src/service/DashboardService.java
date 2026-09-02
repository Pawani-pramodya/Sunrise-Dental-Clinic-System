/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;


import dao.DashboardDAO;

public class DashboardService {

    private DashboardDAO dashboardDAO;

    public DashboardService() {
        dashboardDAO = new DashboardDAO();
    }

    public int getTotalPatients() {

        return dashboardDAO.getTotalPatients();
    }

    public int getTodayAppointments() {

        return dashboardDAO.getTodayAppointments();
    }

    public int getTotalTreatments() {

        return dashboardDAO.getTotalTreatments();
    }
}