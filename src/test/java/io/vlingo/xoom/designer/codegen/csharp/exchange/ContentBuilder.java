// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.csharp.exchange;

import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.template.OutputFile;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;

import java.util.Arrays;
import java.util.List;

public class ContentBuilder {

  public static Content authorContent() {
    return Content.with(CsharpTemplateStandard.AGGREGATE_PROTOCOL, new OutputFile("", "IAuthor.cs"), null, null, AUTHOR_CONTENT_TEXT);
  }

  public static Content authorEntityContent() {
    return Content.with(CsharpTemplateStandard.AGGREGATE, new OutputFile("", "AuthorEntity.cs"), null, null, AUTHOR_ENTITY_CONTENT_TEXT);
  }

  public static Content bookContent() {
    return Content.with(CsharpTemplateStandard.AGGREGATE_PROTOCOL, new OutputFile("", "IBook.cs"), null, null, BOOK_CONTENT_TEXT);
  }

  public static Content authorDataObjectContent() {
    return Content.with(CsharpTemplateStandard.DATA_OBJECT, new OutputFile("", "AuthorData.cs"), null, null, AUTHOR_DATA_CONTENT_TEXT);
  }

  public static Content authorRatedEvent() {
    return Content.with(CsharpTemplateStandard.DOMAIN_EVENT, new OutputFile("", "AuthorRated.cs"), null, null, AUTHOR_RATED_CONTENT_TEXT);
  }

  public static Content authorBlockedEvent() {
    return Content.with(CsharpTemplateStandard.DOMAIN_EVENT, new OutputFile("", "AuthorBlocked.cs"), null, null, AUTHOR_BLOCKED_CONTENT_TEXT);
  }

  public static Content bookSoldOutEvent() {
    return Content.with(CsharpTemplateStandard.DOMAIN_EVENT, new OutputFile("", "BookSoldOut.cs"), null, null, BOOK_SOLD_OUT_CONTENT_TEXT);
  }

  public static Content bookPurchasedEvent() {
    return Content.with(CsharpTemplateStandard.DOMAIN_EVENT, new OutputFile("", "BookPurchased.cs"), null, null, BOOK_PURCHASED_CONTENT_TEXT);
  }

  public static Content rankValueObject() {
    return Content.with(CsharpTemplateStandard.VALUE_OBJECT, new OutputFile("", "Rank.cs"), null, null, RANK_VALUE_OBJECT_CONTENT_TEXT);
  }

  public static Content nameValueObject() {
    return Content.with(CsharpTemplateStandard.VALUE_OBJECT, new OutputFile("", "Name.cs"), null, null, NAME_VALUE_OBJECT_CONTENT_TEXT);
  }

  public static Content classificationValueObject() {
    return Content.with(CsharpTemplateStandard.VALUE_OBJECT, new OutputFile("", "Classification.cs"), null, null, CLASSIFICATION_VALUE_OBJECT_CONTENT_TEXT);
  }

  public static Content classifierValueObject() {
    return Content.with(CsharpTemplateStandard.VALUE_OBJECT, new OutputFile("", "Classifier.cs"), null, null, CLASSIFIER_VALUE_OBJECT_CONTENT_TEXT);
  }

  public static List<Content> contents() {
    return Arrays.asList(authorContent(), authorEntityContent(), bookContent(), authorDataObjectContent(),
        authorRatedEvent(), authorBlockedEvent(), bookSoldOutEvent(), bookPurchasedEvent(),
        rankValueObject(), nameValueObject(), classificationValueObject(), classifierValueObject());
  }

  private static final String AUTHOR_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model.Author; \\n" +
          "public interface IAuthor { \\n" +
          "... \\n" +
          "}";

  private static final String BOOK_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model.Book; \\n" +
          "public interface Book { \\n" +
          "... \\n" +
          "}";

  private static final String AUTHOR_ENTITY_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model.Author; \\n" +
          "public class AuthorEntity { \\n" +
          "... \\n" +
          "}";

  private static final String AUTHOR_DATA_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Infrastructure; \\n" +
          "public class AuthorData { \\n" +
          "... \\n" +
          "}";

  private static final String AUTHOR_RATED_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model.Author; \\n" +
          "public class AuthorRated extends DomainEvent { \\n" +
          "... \\n" +
          "}";

  private static final String AUTHOR_BLOCKED_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model.Author; \\n" +
          "public class AuthorRated extends DomainEvent { \\n" +
          "... \\n" +
          "}";

  private static final String BOOK_SOLD_OUT_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model.Book; \\n" +
          "public class BookSoldOut extends DomainEvent { \\n" +
          "... \\n" +
          "}";

  private static final String BOOK_PURCHASED_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model.Book; \\n" +
          "public class BookPurchased extends DomainEvent { \\n" +
          "... \\n" +
          "}";

  private static final String NAME_VALUE_OBJECT_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model; \\n" +
          "public class Name { \\n" +
          "... \\n" +
          "}";
  private static final String CLASSIFICATION_VALUE_OBJECT_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model; \\n" +
          "public class Classification { \\n" +
          "... \\n" +
          "}";
  private static final String CLASSIFIER_VALUE_OBJECT_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model; \\n" +
          "public class Classifier { \\n" +
          "... \\n" +
          "}";
  private static final String RANK_VALUE_OBJECT_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model; \\n" +
          "public class Rank { \\n" +
          "... \\n" +
          "}";
}
