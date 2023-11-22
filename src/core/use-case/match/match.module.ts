import { Module } from "@nestjs/common";
import { MatchCreate } from "./match-create";
import { MatchDelete } from "./match-delete";
import { MatchPage } from "./match-page";
import { MatchUpdate } from "./match-update"

const providers = [MatchCreate, MatchDelete, MatchPage, MatchUpdate];

@Module({ providers, exports: [...providers] })
export class MatchModule {}
