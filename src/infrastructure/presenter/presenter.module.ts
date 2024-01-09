import { Module } from "@nestjs/common";
import { TournamentPagePresenter } from "./tournament/page/tournament-page.presenter";
import { UseCasesModule } from "src/core/use-case/use-cases.module";
import { TournamentUpdatePresenter } from "./tournament/update/tournament-update";
import { TournamentCreatePresenter } from "./tournament/create/tournament-create.presenter";
import { TournamentDeletePresenter } from "./tournament/delete/tournament-delete.presenter";
import { MatchPagePresenter } from "./match/page/match-page.presenter";
import { MatchCreatePresenter } from "./match/create/match-create.presenter";
import { MatchDeletePresenter } from "./match/delete/match-delete.presenter";
import { MatchUpdatePresenter } from "./match/update/match-update";
import { RefereesCreatePresenter } from "./referees/create/referees-create.presenter";
import { RefereesDeletePresenter } from "./referees/delete/referees-delete.presenter";
import { RefereesPagePresenter } from "./referees/page/referees-page.presenter";
import { RefereesUpdatePresenter } from "./referees/update/referees-update";

const providers = [
  //TOURNAMENT
  TournamentPagePresenter,
  TournamentCreatePresenter,
  TournamentDeletePresenter,
  TournamentUpdatePresenter,

  //MATCH
  MatchPagePresenter,
  MatchCreatePresenter,
  MatchDeletePresenter,
  MatchUpdatePresenter,

  //REFEREES
  RefereesCreatePresenter,
  RefereesDeletePresenter,
  RefereesPagePresenter,
  RefereesUpdatePresenter,

  //TEAMS
  //USER
  UseCasesModule,
];
@Module({
  imports: [UseCasesModule],
  providers,
  exports: [...providers],
})
export class PresenterModule {}
