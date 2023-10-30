import { Module } from "@nestjs/common";
import { ControllerModule } from "./infrastructure/controller/controller.module";
import { AppController } from "./infrastructure/controller/healtz.controller";

@Module({
    imports:[ControllerModule],
    controllers:[AppController]
})
export class AppModule{}