import { Module } from "@nestjs/common";
import { MatchCreate } from "./match-create";
import { MatchDelete } from "./match-delete";
import { MatchPage } from "./match-page";
import { MatchRestore } from "./match-restore";

const providers = [MatchCreate, MatchDelete, MatchPage, MatchRestore];

@Module({ providers, exports: [...providers] })
export class MatchModule {}
