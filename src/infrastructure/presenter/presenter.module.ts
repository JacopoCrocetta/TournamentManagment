import { Module } from "@nestjs/common";
import { TournamentPresenter } from "./tournament/tournament.presenter";

const providers = [TournamentPresenter];
@Module({providers, exports:[...providers]})
export class PresenterModule{}