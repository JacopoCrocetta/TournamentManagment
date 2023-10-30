import { Module } from "@nestjs/common";
import { AppController } from "./healtz.controller";
import { TorunamentController } from "./tournament.controller";

const controllers = [AppController, TorunamentController];

@Module({
  controllers,
})
export class ControllerModule {}
