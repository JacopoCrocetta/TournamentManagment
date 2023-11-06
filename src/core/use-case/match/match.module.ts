import { Module } from "@nestjs/common";
import { MatchCreate } from "./match-create";
import { MatchDelete } from "./match-delete";
import { MatchPage } from "./match-page";

const providers = [MatchCreate, MatchDelete, MatchPage];

@Module({ providers, exports: [...providers] })
export class MatchModule {}
