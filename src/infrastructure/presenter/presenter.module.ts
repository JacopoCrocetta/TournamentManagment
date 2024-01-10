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
import { StandingsCreatePresenter } from "./standings/create/standings-create.presenter";
import { StandingsDeletePresenter } from "./standings/delete/standings-delete.presenter";
import { StandingsPagePresenter } from "./standings/page/standings-page.presenter";
import { StandingsUpdatePresenter } from "./standings/update/standings-update";
import { TeamsCreatePresenter } from "./teams/create/teams-create.presenter";
import { TeamsDeletePresenter } from "./teams/delete/teams-delete.presenter";
import { TeamsPagePresenter } from "./teams/page/teams-page.presenter";
import { UserCreatePresenter } from "./user/create/user-create.presenter";
import { UserDeletePresenter } from "./user/delete/user-delete.presenter";
import { TeamsUpdatePresenter } from "./teams/update/teams-update";
import { UserPagePresenter } from "./user/page/user-page.presenter";
import { UserUpdatePresenter } from "./user/update/user-update";

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

  //STANDINGS
  StandingsCreatePresenter,
  StandingsDeletePresenter,
  StandingsPagePresenter,
  StandingsUpdatePresenter,
  
  //TEAMS
  TeamsCreatePresenter,
  TeamsDeletePresenter,
  TeamsPagePresenter,
  TeamsUpdatePresenter,
  
  //USER
  UserCreatePresenter,
  UserDeletePresenter,
  UserPagePresenter,
  UserUpdatePresenter,

  UseCasesModule,
];
@Module({
  imports: [UseCasesModule],
  providers,
  exports: [...providers],
})
export class PresenterModule {}
