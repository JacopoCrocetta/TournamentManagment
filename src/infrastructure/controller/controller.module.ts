import { Module } from "@nestjs/common";
import { TournamentController } from "./tournament.controller";
import { AppController } from "./healtz.controller";

const controllers = [TournamentController, AppController];

@Module({
  controllers,
  exports: [...controllers]
})
export class ControllerModule {}
