import { Module } from "@nestjs/common";
import { TournamentController } from "./tournament.controller";
import { PresenterModule } from "../presenter/presenter.module";

const controllers = [TournamentController];

@Module({
  controllers,
  imports:[PresenterModule]
})
export class ControllerModule {}
