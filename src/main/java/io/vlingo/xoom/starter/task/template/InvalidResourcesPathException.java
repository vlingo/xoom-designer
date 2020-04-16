// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template;

public class InvalidResourcesPathException extends TemplateGenerationException {

    public InvalidResourcesPathException() {
        super("Please check if the starter path has been set in the environment variables");
    }

}
