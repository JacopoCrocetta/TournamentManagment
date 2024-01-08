import { Module } from "@nestjs/common";
import { TournamentPagePresenter } from "./tournament/page/tournament-page.presenter";
import { UseCasesModule } from "src/core/use-case/use-cases.module";
import { TournamentUpdatePresenter } from "./tournament/update/tournament-update";
import { TournamentCreatePresenter } from "./tournament/create/tournament-create.presenter";
import { TournamentDeletePresenter } from "./tournament/delete/tournament-delete.presenter";

const providers = [TournamentPagePresenter, TournamentCreatePresenter, TournamentDeletePresenter, TournamentUpdatePresenter, UseCasesModule];
@Module({
  imports:[UseCasesModule],
  providers,
  exports: [...providers],
})
export class PresenterModule {}
