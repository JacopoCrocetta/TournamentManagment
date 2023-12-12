import { Module } from "@nestjs/common";
import { TournamentPagePresenter } from "./tournament/page/tournament-page.presenter";
import { UseCasesModule } from "src/core/use-case/use-cases.module";

const providers = [TournamentPagePresenter, UseCasesModule];
@Module({
  imports:[UseCasesModule],
  providers,
  exports: [...providers],
})
export class PresenterModule {}
