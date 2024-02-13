import { Module } from "@nestjs/common";
import { RefereesCreate } from "./referees-create";
import { RefereesDelete } from "./referees-delete";
import { RefereesPage } from "./referees-page";
import { RefereesUpdate } from "./referees-update"

const providers = [RefereesCreate, RefereesDelete, RefereesPage];

@Module({ providers, exports: { ...providers } })
export class RefereesModule {}
