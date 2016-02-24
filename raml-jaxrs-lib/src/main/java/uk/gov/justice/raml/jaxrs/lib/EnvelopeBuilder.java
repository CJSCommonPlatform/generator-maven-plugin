package uk.gov.justice.raml.jaxrs.lib;

import javax.json.JsonObject;
import javax.ws.rs.core.HttpHeaders;

/**
 * Created by david on 15/02/16.
 */
public interface EnvelopeBuilder {

    Envelope<JsonObject> build();

    EnvelopeBuilder withMetadataFrom(HttpHeaders headers);

    EnvelopeBuilder withBody(JsonObject body);

}
