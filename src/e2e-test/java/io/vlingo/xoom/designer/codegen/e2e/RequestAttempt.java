// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.e2e;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.vlingo.xoom.actors.Logger;
import org.junit.jupiter.api.Assertions;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class RequestAttempt {

  private int retries;
  private int interval;
  private final String requestName;
  private Predicate<JsonPath> responseCondition;

  public static RequestAttempt of(final String requestName) {
    return new RequestAttempt(requestName).retries(10).interval(500);
  }

  private RequestAttempt(final String requestName) {
    this.requestName = requestName;
  }

  public RequestAttempt acceptResponseWhen(final Predicate<JsonPath> responseCondition) {
    this.responseCondition = responseCondition;
    return this;
  }

  public RequestAttempt retries(final int retries) {
    this.retries = retries;
    return this;
  }

  public RequestAttempt interval(final int interval) {
    this.interval = interval;
    return this;
  }

  public Response perform(final Supplier<Response> request) {
    Assertions.assertNotNull(responseCondition, "Must exist a response condition");
    int retried = 0;
    do {
      final Response response = request.get();
      if (responseCondition.test(response.jsonPath())) {
        return response;
      }
      retried++;
      pause(interval);
    } while (retried <= retries);
    throw new RequestAttemptException(String.format("Attempt(s) of %s failed", requestName));
  }

  private void pause(final int milliseconds) {
    try {
      Thread.sleep(milliseconds);
    } catch (final InterruptedException exception) {
      Logger.basicLogger().warn("Unable to wait " + milliseconds + " ms", exception);
    }
  }

}
