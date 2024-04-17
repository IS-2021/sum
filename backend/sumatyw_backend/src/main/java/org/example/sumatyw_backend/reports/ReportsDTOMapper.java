package org.example.sumatyw_backend.reports;

import org.example.sumatyw_backend.restaurants.Restaurant;
import org.example.sumatyw_backend.user_reports.RestaurantReport;
import org.example.sumatyw_backend.user_reports.UserReport;
import org.example.sumatyw_backend.users.User;

import java.time.LocalDateTime;

public class ReportsDTOMapper {

    public static UserReport mapInputReportToUserReport(ReportInputDTO reportInputDTO,User user,Restaurant restaurant) {

        return UserReport.builder()
            .user(user)
            .restaurant(restaurant)
            .cause(reportInputDTO.cause())
            .timestamp(String.valueOf(LocalDateTime.now()))
            .build();

    }

    public static RestaurantReport mapInputReportToRestaurantReport(ReportInputDTO reportInputDTO,User user,Restaurant restaurant) {

        return RestaurantReport.builder()
            .user(user)
            .restaurant(restaurant)
            .cause(reportInputDTO.cause())
            .timestamp(String.valueOf(LocalDateTime.now()))
            .build();
    }

    public static ReportDTO mapRestaurantReportToReportDTO(RestaurantReport restaurantReport) {
        return new ReportDTO(
            restaurantReport.getRestaurantReportId(),
            restaurantReport.getUser().getUserId(),
            restaurantReport.getRestaurant().getRestaurantId(),
            restaurantReport.getCause(),
            restaurantReport.getTimestamp(),
            restaurantReport.isOpen()
        );
    }

    public static ReportDTO mapUserReportToReportDTO(UserReport userReport) {
        return new ReportDTO(
            userReport.getUserReportId(),
            userReport.getUser().getUserId(),
            userReport.getRestaurant().getRestaurantId(),
            userReport.getCause(),
            userReport.getTimestamp(),
            userReport.isOpen()
        );
    }
}
