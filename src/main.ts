import { NestFactory } from "@nestjs/core";
import { AppModule } from "./app.module";
import { DocumentBuilder, SwaggerModule } from "@nestjs/swagger";
import { INestApplication, Logger } from "@nestjs/common";

async function bootstrap(): Promise<INestApplication> {

  Logger.log("CREATING APPMODULE...");
  const app = await NestFactory.create(AppModule);

  
  Logger.log("INITIALIZING THE SWAGGER...");
  const swaggerConfig = new DocumentBuilder()
    .setTitle("Tournament Managment")
    .setDescription("Tournament managment system")
    .setVersion("1.0")
    .build();

  Logger.log("CREATION OF THE SWAGGER DOCUMENT...");
  const document = SwaggerModule.createDocument(app, swaggerConfig);
  
  Logger.log("SETUP THE SWAGGER...");
  SwaggerModule.setup("api", app, document);

  Logger.log("APP IS LISTEN AT PORT 4000...");
  await app.listen(4000);

  return app;
}

bootstrap().then(() => {});
