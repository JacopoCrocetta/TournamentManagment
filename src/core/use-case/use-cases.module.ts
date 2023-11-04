import { Module } from "@nestjs/common";
import { MatchModule } from "./match/match.module";

const providers = [MatchModule];
@Module({ providers, exports: [...providers] })
export class UseCasesModule {}
