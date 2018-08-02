package muhammad.shulhi.muhibush.vetsub.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Afdolash on 10/22/2017.
 */

public class Event implements Serializable {
    private int id_event;
    private String title_event;
    private String description_event;
    private double latitude_event;
    private double longitude_event;
    private String location_event;
    private String timeStart_event;
    private String dateStart_event;
    private String fee_event;
    private String timeEnd_event;
    private String dateEnd_event;
    private String category_event;
    private String image_event;
    private String url_event;
    private String contact_event;
    private String created_at;

    private String source_event;
    private int enthusiasts_event;
    private String author_event;
    private int viewer_event;
    private int report_event;
    private int block_event;
    private String id_user;
    private String status_event;
    private String description_status_event;

    public Event() {
    }

    public Event(int id_event, String title_event, String description_event, double latitude_event, double longitude_event, String location_event, String timeStart_event, String dateStart_event, String timeEnd_event, String dateEnd_event, String category_event, String image_event, String url_event, String contact_event) {
        this.id_event = id_event;
        this.title_event = title_event;
        this.description_event = description_event;
        this.latitude_event = latitude_event;
        this.longitude_event = longitude_event;
        this.location_event = location_event;
        this.timeStart_event = timeStart_event;
        this.dateStart_event = dateStart_event;
        this.timeEnd_event = timeEnd_event;
        this.dateEnd_event = dateEnd_event;
        this.category_event = category_event;
        this.image_event = image_event;
        this.url_event = url_event;
        this.contact_event = contact_event;
    }

    public Event(int id_event, String title_event, String description_event, double latitude_event, double longitude_event, String location_event, String timeStart_event, String dateStart_event, String timeEnd_event, String dateEnd_event, String category_event, String image_event, String url_event, String contact_event, String status_event) {
        this.id_event = id_event;
        this.title_event = title_event;
        this.description_event = description_event;
        this.latitude_event = latitude_event;
        this.longitude_event = longitude_event;
        this.location_event = location_event;
        this.timeStart_event = timeStart_event;
        this.dateStart_event = dateStart_event;
        this.timeEnd_event = timeEnd_event;
        this.dateEnd_event = dateEnd_event;
        this.category_event = category_event;
        this.image_event = image_event;
        this.url_event = url_event;
        this.contact_event = contact_event;
        this.status_event = status_event;
    }

    public Event(String title_event, String description_event, double latitude_event, double longitude_event, String location_event, String timeStart_event, String dateStart_event, String timeEnd_event, String dateEnd_event, String category_event, String image_event, String source_event, int enthusiasts_event, String author_event, int viewer_event, int report_event, int block_event) {
        this.title_event = title_event;
        this.description_event = description_event;
        this.latitude_event = latitude_event;
        this.longitude_event = longitude_event;
        this.location_event = location_event;
        this.timeStart_event = timeStart_event;
        this.dateStart_event = dateStart_event;
        this.timeEnd_event = timeEnd_event;
        this.dateEnd_event = dateEnd_event;
        this.category_event = category_event;
        this.image_event = image_event;
        this.source_event = source_event;
        this.enthusiasts_event = enthusiasts_event;
        this.author_event = author_event;
        this.viewer_event = viewer_event;
        this.report_event = report_event;
        this.block_event = block_event;
    }

    public String getStatus_event() {
        return status_event;
    }

    public void setStatus_event(String status_event) {
        this.status_event = status_event;
    }

    public int getId_event() {
        return id_event;
    }

    public String getTitle_event() {
        return title_event;
    }

    public String getDescription_event() {
        return description_event;
    }

    public double getLatitude_event() {
        return latitude_event;
    }

    public double getLongitude_event() {
        return longitude_event;
    }

    public String getTimeStart_event() {
        return timeStart_event;
    }

    public String getDateStart_event() {
        return dateStart_event;
    }

    public String getTimeEnd_event() {
        return timeEnd_event;
    }

    public String getDateEnd_event() {
        return dateEnd_event;
    }

    public String getCategory_event() {
        return category_event;
    }

    public String getImage_event() {
        return image_event;
    }

    public String getSource_event() {
        return source_event;
    }

    public int getEnthusiasts_event() {
        return enthusiasts_event;
    }

    public String getAuthor_event() {
        return author_event;
    }

    public int getViewer_event() {
        return viewer_event;
    }

    public int getReport_event() {
        return report_event;
    }

    public int getBlock_event() {
        return block_event;
    }

    public String getLocation_event() {
        return location_event;
    }

    public String getId_user() {
        return id_user;
    }


    public String getUrl_event() {
        return url_event;
    }

    public String getContact_event() {
        return contact_event;
    }

    public String getDescription_status_event() {
        return description_status_event;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getFee_event() {
        return fee_event;
    }
}
