interface Pass {

    boolean isValid();
}

class OneDayVisitorPass implements Pass {

    private final boolean valid;

    public OneDayVisitorPass(boolean valid) {
        this.valid = valid;
    }

    @Override
    public boolean isValid() {
        return valid;
    }
}
