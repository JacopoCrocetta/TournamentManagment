import { Module } from "@nestjs/common";

const providers = []

@Module({providers, exports:{...providers}})
export class RefereesModule{}