import { Module } from "@nestjs/common";
import { ControllerModule } from "./infrastructure/controller/controller.module";
import { AppController } from "./infrastructure/controller/healtz.controller";
import { PrismaService } from "./infrastructure/database/prisma.service";

@Module({
    imports:[ControllerModule],
    controllers:[AppController],
    providers: [PrismaService]
})
export class AppModule{}