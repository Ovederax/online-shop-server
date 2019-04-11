package net.thumbtack.onlineshop.dto.response;

import java.util.Objects;

public class AvailableSettingResponse {
    private int maxNameLength;
    private int minPasswordLength;

    public AvailableSettingResponse(int maxNameLength, int minPasswordLength) {
        this.maxNameLength = maxNameLength;
        this.minPasswordLength = minPasswordLength;
    }

    public AvailableSettingResponse() {
    }

    public int getMaxNameLength() {
        return maxNameLength;
    }

    public void setMaxNameLength(int maxNameLength) {
        this.maxNameLength = maxNameLength;
    }

    public int getMinPasswordLength() {
        return minPasswordLength;
    }

    public void setMinPasswordLength(int minPasswordLength) {
        this.minPasswordLength = minPasswordLength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AvailableSettingResponse)) return false;
        AvailableSettingResponse that = (AvailableSettingResponse) o;
        return getMaxNameLength() == that.getMaxNameLength() &&
                getMinPasswordLength() == that.getMinPasswordLength();
    }

    @Override
    public int hashCode() {

        return Objects.hash(getMaxNameLength(), getMinPasswordLength());
    }
}
