package uk.gov.justice.domain.eventsource;

public class Location {
    private final String jmsUri;
    private final String restUri;
    private final String dataSource;

    public Location(final String jmsUri, final String restUri,
                    final String dataSource) {
        this.jmsUri = jmsUri;
        this.restUri = restUri;
        this.dataSource = dataSource;
    }

    public String getJmsUri() {
        return jmsUri;
    }

    public String getRestUri() {
        return restUri;
    }

    public String getDataSource() {
        return dataSource;
    }
}
