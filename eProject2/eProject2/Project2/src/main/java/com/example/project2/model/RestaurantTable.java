package com.example.project2.model;

public class RestaurantTable {
    private int tableId;
    private String tableName;
    private int capacity;
    private String status; // available, occupied, reserved
    private int positionX;
    private int positionY;
    private boolean selected;

    public RestaurantTable(int tableId, String tableName, int capacity,
                           String status, int positionX, int positionY) {
        this.tableId = tableId;
        this.tableName = tableName;
        this.capacity = capacity;
        this.status = status;
        this.positionX = positionX;
        this.positionY = positionY;
        this.selected = false;
    }

    public int getTableId() { return tableId; }
    public void setTableId(int tableId) { this.tableId = tableId; }

    public String getTableName() { return tableName; }
    public void setTableName(String tableName) { this.tableName = tableName; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getPositionX() { return positionX; }
    public void setPositionX(int positionX) { this.positionX = positionX; }

    public int getPositionY() { return positionY; }
    public void setPositionY(int positionY) { this.positionY = positionY; }

    public boolean isSelected() { return selected; }
    public void setSelected(boolean selected) { this.selected = selected; }

    @Override
    public String toString() {
        return tableName + " (" + capacity + " chỗ)";
    }
}